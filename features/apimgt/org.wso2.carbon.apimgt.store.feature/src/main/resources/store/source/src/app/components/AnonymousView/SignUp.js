/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import React from 'react'
import Grid from 'material-ui/Grid';
import TextField from 'material-ui/TextField';
import Typography from 'material-ui/Typography';
import Button from 'material-ui/Button';
import PropTypes from 'prop-types';
import {withStyles} from 'material-ui/styles';
import {FormControl, FormControlLabel} from 'material-ui/Form';
import Paper from 'material-ui/Paper';
import { InputAdornment} from 'material-ui/Input';
import User from '@material-ui/icons/AccountCircle';
import Lock from '@material-ui/icons/Lock';
import Person from '@material-ui/icons/Person';
import Mail from '@material-ui/icons/Mail';
import { Link } from 'react-router-dom';
import AuthManager from "../../data/AuthManager";
import Utils from "../../data/Utils";
import ConfigManager from "../../data/ConfigManager";
import LoadingAnimation from "../Base/Loading/Loading";
import API from "../../data/api";
import Checkbox from 'material-ui/Checkbox';
import Alert from '../Shared/Alert';

const styles = {
    buttonsWrapper: {
        marginTop: 10,
        marginLeft:10
    },
    buttonAlignment: {
        marginLeft: 20,
    }
};

class SignUp extends React.Component{
    constructor(props) {
        super(props);
        this.authManager = new AuthManager();
        this.state = {
            environments: [],
            environmentId: 0,
            username: "",
            password: "",
            firstName: "",
            lastName: "",
            email: "",
            errorMessage: "",
            error: false,
            policy: false,
            validation: false,
            validationError: ""
        };
    }

    componentDidMount(){
        ConfigManager.getConfigs().environments.then(response => {
            const environments = response.data.environments;
            let environmentId = Utils.getEnvironmentID(environments);
            if (environmentId === -1) {
                environmentId = 0;
            }
            this.setState({
                environments: environments,
                environmentId: environmentId
            });
        }).catch(() => {
            console.error('Error while receiving environment configurations');
        });
    };

    handleClick = () => {
        this.handleAuthentication().then(
            () => this.handleSignUp()
        ).catch(() =>
            console.log("Error occurred during authentication")
        );
    };

    handleSignUp = () => {
        let { username, password, firstName, lastName, email, error } = this.state;
        if (!username || !password || !firstName || !lastName || !email || error){
            if (error) {
                Alert.warning('Please re-check password');
            } else {
                Alert.warning('Please fill all required fields');
            }
        } else {
            let user_data = {
                username: username.toLowerCase(),
                password: password,
                firstName: firstName,
                lastName: lastName,
                email: email
            };
            let api = new API();
            let promise = api.createUser(user_data);
            promise.then(() => {
                console.log("User created successfully.");
                this.authManager.logout();
                Alert.info("User added successfully. You can now sign into the API store.");
                let redirect_url = "/login";
                this.props.history.push(redirect_url);
            }).catch(() => {
                console.log("Error while creating user");
            })
        }
    };

    handleAuthentication = () => {
        const { environments, environmentId } = this.state;
        return this.authManager.registerUser(environments[environmentId]);
    };

    handleChange = name => event => {
        this.setState({ [name]: event.target.value });
    };

    handlePasswordChange = () => event => {
        if (event.target.value !== this.state.password) {
            this.setState({
                error: true,
                errorMessage: "Password does not match"
            })
        } else {
            this.setState({
                error: false,
                errorMessage: ""
            })
        }
    };

    handlePasswordValidation= name => event => {
        let password = event.target.value;
        let regex = new RegExp("^(?=.*[A-Z])(?=.*[0-9])(?=.{8,})");
        if (!regex.test(password)) {
            this.setState({
                validation: true,
                validationError: "Password must contain minimum 8 characters, at least one upper case letter and one number"
            });
        } else {
            this.setState({
                validation: false,
                validationError: "",
                [name]: event.target.value
            });
        }
    };

    handlePolicyChange  = (event) => {
        if (event.target.checked) {
            this.setState({ policy: true });
        } else {
            this.setState({ policy: false });
        }
    };

    render(){
        const { classes } = this.props;
        const { environments, error, errorMessage, policy, environmentId, validation, validationError } = this.state;
        if (!environments[environmentId]) {
            return <LoadingAnimation/>
        }
        return(
            <div className="login-flex-container">
                <Grid container justify={"center"} alignItems={"center"} spacing={0} style={{height: "100vh"}}>
                    <Grid item lg={6} md={8} xs={10}>
                        <Grid container>
                            <Grid item sm={3} xs={12}>
                                <Grid container direction={"column"}>
                                    <Grid item>
                                        <img className="brand"
                                             src={`/store/public/app/images/logo.svg`}
                                             alt="wso2-logo"/>
                                    </Grid>
                                    <Grid item>
                                        <Typography type="subheading" align="right" gutterBottom>
                                            API STORE
                                        </Typography>
                                    </Grid>
                                </Grid>
                            </Grid>

                            {/*Sign-up Form*/}
                            <Grid item sm={9} xs={12}>
                                <div className="login-main-content">
                                    <Paper elevation={1} square={true} className="login-paper">
                                        <form className="login-form">
                                            <Typography type="body1" gutterBottom>
                                                Create your account
                                            </Typography>
                                            <span>
                                                <FormControl style={{width: "100%"}}>
                                                    <TextField
                                                        required
                                                        id="username"
                                                        label="Username"
                                                        type="text"
                                                        autoComplete="username"
                                                        margin="normal"
                                                        style={{width: "100%"}}
                                                        onChange={this.handleChange('username')}
                                                        InputProps={{
                                                            startAdornment: (
                                                                <InputAdornment position="start">
                                                                    <User />
                                                                </InputAdornment>
                                                            ),
                                                        }}
                                                    />
                                                    <TextField
                                                        required
                                                        id="password"
                                                        label="Password"
                                                        type="password"
                                                        autoComplete="current-password"
                                                        margin="normal"
                                                        style={{width: "100%"}}
                                                        onChange={ this.handlePasswordValidation('password') }
                                                        InputProps={{
                                                            startAdornment: (
                                                                <InputAdornment position="start">
                                                                    <Lock />
                                                                </InputAdornment>
                                                            )
                                                        }}
                                                        error={validation}
                                                        helperText={validationError}
                                                    />
                                                    <TextField
                                                        required
                                                        error={error}
                                                        id="rePassword"
                                                        label="Re-type Password"
                                                        type="password"
                                                        autoComplete="current-password"
                                                        margin="normal"
                                                        style={{width: "100%"}}
                                                        onChange={this.handlePasswordChange('rePassword')}
                                                        InputProps={{
                                                            startAdornment: (
                                                                <InputAdornment position="start">
                                                                    <Lock />
                                                                </InputAdornment>
                                                            )
                                                        }}
                                                        helperText={errorMessage}
                                                    />
                                                    <TextField
                                                        required
                                                        id="firstName"
                                                        label="First Name"
                                                        type="text"
                                                        margin="normal"
                                                        style={{width: "100%"}}
                                                        onChange={this.handleChange('firstName')}
                                                        InputProps={{
                                                            startAdornment: (
                                                                <InputAdornment position="start">
                                                                    <Person />
                                                                </InputAdornment>
                                                            ),
                                                        }}
                                                    />
                                                    <TextField
                                                        required
                                                        id="lastName"
                                                        label="Last Name"
                                                        type="text"
                                                        margin="normal"
                                                        style={{width: "100%"}}
                                                        onChange={this.handleChange('lastName')}
                                                        InputProps={{
                                                            startAdornment: (
                                                                <InputAdornment position="start">
                                                                    <Person />
                                                                </InputAdornment>
                                                            ),
                                                        }}
                                                    />
                                                    <TextField
                                                        required
                                                        id="email"
                                                        label="E mail"
                                                        type="email"
                                                        margin="normal"
                                                        style={{width: "100%"}}
                                                        onChange={this.handleChange('email')}
                                                        InputProps={{
                                                            startAdornment: (
                                                                <InputAdornment position="start">
                                                                    <Mail />
                                                                </InputAdornment>
                                                            ),
                                                        }}
                                                    />
                                                    <FormControl>
                                                    <Typography>
                                                        After successfully signing in, a cookie is placed in your browser to track your session. See our {' '}
                                                        <Link to={"/policy/cookie-policy"} target="_blank">
                                                              Cookie Policy
                                                        </Link>
                                                        {' '} for more details.
                                                    </Typography>
                                                    </FormControl>
                                                    <FormControlLabel
                                                        control={
                                                            <Checkbox onChange={this.handlePolicyChange} />
                                                        }
                                                        label={
                                                            <p>
                                                                <strong>
                                                                    I hereby confirm that I have read and understood the {''}
                                                                    <Link to={"/policy/privacy-policy"} target="_blank">
                                                                        Privacy Policy.
                                                                    </Link>
                                                                </strong>
                                                            </p>
                                                        }
                                                    />
                                                </FormControl>
                                            </span>
                                            <div className={classes.buttonsWrapper}>
                                                <Button
                                                    variant="raised"
                                                    color="primary"
                                                    onClick={this.handleClick.bind(this)}
                                                    disabled={!policy}
                                                >
                                                    Sign up
                                                </Button>
                                                <Link to={"/"} style={{ textDecoration: 'none' }}>
                                                    <Button variant="raised" className={classes.buttonAlignment}>
                                                        Back to Store
                                                    </Button>
                                                </Link>
                                            </div>
                                        </form>
                                    </Paper>
                                </div>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </div>
        );
    }
}

SignUp.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(SignUp);
