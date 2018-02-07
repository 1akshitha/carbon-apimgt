/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
import Button from 'material-ui/Button';
import Card, { CardActions, CardContent } from 'material-ui/Card';
import Grid from 'material-ui/Grid';
import Typography from 'material-ui/Typography';
import Api from '../../../../data/api'
import Resource from './Resource'
import Loading from '../../../Base/Loading/Loading'
import ApiPermissionValidation from '../../../../data/ApiPermissionValidation'
import Select from 'material-ui/Select';
import {MenuItem} from 'material-ui/Menu';
import {InputLabel}  from 'material-ui/Input';
import TextField from 'material-ui/TextField';
import { FormGroup, FormControlLabel, FormControl } from 'material-ui/Form';
import Checkbox from 'material-ui/Checkbox';
import { withStyles } from 'material-ui/styles';
import PropTypes from 'prop-types';
import Divider from 'material-ui/Divider';
<<<<<<< HEAD
=======
import projectDate from './ProjectData';
>>>>>>> 6479ab26681e732f5125f7b85f58c96e8632053c
import { LinearProgress } from 'material-ui/Progress';
import Chip from 'material-ui/Chip';
import List, {
    ListItem,
    ListItemText,
    ListItemSecondaryAction
} from 'material-ui/List';
import Delete from 'material-ui-icons/Delete';

const styles = theme => ({
    root: {
        flexGrow: 1,
        marginTop: 10,
    },
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    textField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,
        width: 400,
    },
    mainTitle: {
        paddingLeft: 20
    },
    scopes: {
        width: 400
    },
    divider: {
        marginTop: 20,
        marginBottom: 20
    }
});

class Resources extends React.Component{
    constructor(props){
        super(props);
        this.state ={
            tmpMethods:[],
            tmpResourceName: '',
            paths:{},
            swagger:{},
            scopes:[],
            pathDeleteList: [],
            allChecked: false,
        };
        this.api = new Api();
        this.api_uuid = props.match.params.api_uuid;
        this.addResources = this.addResources.bind(this);
        this.onChange = this.onChange.bind(this);
        this.onChangeInput = this.onChangeInput.bind(this);
        this.updatePath = this.updatePath.bind(this);
        this.addRemoveToDeleteList = this.addRemoveToDeleteList.bind(this);
        this.updateResources = this.updateResources.bind(this);
        this.handleScopeChange = this.handleScopeChange.bind(this);
        this.handleCheckAll = this.handleCheckAll.bind(this);
        this.deleteSelected = this.deleteSelected.bind(this);
        this.childResources = [];

    }
    handleChange = name => event => {
        let tmpMethods = this.state.tmpMethods;
        let index = tmpMethods.indexOf(name);

        if(event.target.checked){
            // add to tmpMethods
            if( index === -1 ) {
                tmpMethods.push(name);
            }
        } else {
            // remove from tmpMethods if exists
            if( index > -1 ){
                tmpMethods.splice(index, 1);
            }
        }
        this.setState({tmpMethods:tmpMethods});

    };
    onChange(checkedValues) {
        this.setState({tmpMethods:checkedValues});
    }
    handleScopeChange(e) {
        this.setState({scopes: e.target.value});
        this.handleScopeChangeInSwaggerRoot(e.target.value);

    }
    handleScopeChangeInSwaggerRoot(scopes){
        let swagger = this.state.swagger;
        if (swagger.security){
            swagger.security.map(function(object, i){
                if(object.OAuth2Security){
                    object.OAuth2Security = scopes;
                }
            });
        } else{
            swagger.security = [{"OAuth2Security":scopes}];
        }
            this.setState({swagger:swagger});
        }


    componentDidMount() {

        const api = new Api();
        let promised_api_object = api.get(this.api_uuid);
        promised_api_object.then(
            response => {
                this.setState({api: response.obj});
            }
        ).catch(
            error => {
                if (process.env.NODE_ENV !== "production") {
                    console.log(error);
                }
                let status = error.status;
                if (status === 404) {
                    this.setState({notFound: true});
                }
            }
        );
        let promised_scopes_object = api.getScopes(this.api_uuid);
        promised_scopes_object.then(
            response => {
                this.setState({apiScopes: response.obj});
            }
        ).catch(
            error => {
                if (process.env.NODE_ENV !== "production") {
                    console.error(error);
                }
                let status = error.status;
                if (status === 404) {
                    this.setState({notFound: true});
                }
            }
        );

        let promised_api = this.api.getSwagger(this.api_uuid);
        promised_api.then((response) => {
            let tempScopes = [];
            if(response.obj.security && response.obj.security.length!== 0){
                response.obj.security.map(function(object, i){
                    if(object.OAuth2Security){
                        tempScopes =  object.OAuth2Security;
                    }
                });
            }
            this.setState({swagger: response.obj, scopes: tempScopes});

            if(response.obj.paths !== undefined ){
                this.setState({paths:response.obj.paths})
            }
        }).catch(error => {
            if (process.env.NODE_ENV !== "production")
                console.log(error);
            let status = error.status;
            if (status === 404) {
                this.setState({notFound: true});
            } else if (status === 401) {
                this.setState({isAuthorize: false});
                let params = qs.stringify({reference: this.props.location.pathname});
                this.props.history.push({pathname: "/login", search: params});
            }
        });
    }
    onChangeInput = name => event => {
        let value = event.target.value;
        if(value.indexOf("/") === -1 ){
            value = "/" + value;
        }
        this.setState({[name]: value});
    }
    addResources(){
        let allMehtods = ["get","put","post","delete","patch","head"];
        const defaultGet =  {
            description:'description',
            produces:'application/xml,application/json',
            consumes:'application/xml,application/json',
            parameters:[],
            responses: {
                200: {
                    "description": ""
                }
            }
        };

        const defaultPost =  {
            description: 'description',
            produces: 'application/xml,application/json',
            consumes: 'application/xml,application/json',
            responses: {
                200: {
                    "description": ""
                }
            },
            parameters: [
                {
                    name: "Payload",
                    description: "Request Body",
                    required: false,
                    in: "body",
                    schema: {
                        type: "object",
                        properties: {
                            payload: {
                                type: "string"
                            }
                        }
                    }
                }
            ]
        };

        const defaultDelete =  {
            description: 'description',
            produces: 'application/xml,application/json',
            responses: {
                200: {
                    "description": ""
                }
            },
            parameters: []
        };
        const defaultHead =  {
            responses: {
                200: {
                    "description": ""
                }
            },
            parameters: []
        };
        let pathValue = {};
        let existingPathVale = {};
        let tmpPaths = this.state.paths;
        if(Object.keys(tmpPaths).length >0 ){
            if(this.state.tmpResourceName in tmpPaths){
                existingPathVale = tmpPaths[this.state.tmpResourceName];
            }
        }
        allMehtods.map( (method ) => {
            switch (method) {
                case "get" :
                    if("get" in existingPathVale){
                        pathValue["get"] = existingPathVale["get"];
                    } else if(this.state.tmpMethods.indexOf("get") !== -1){
                        pathValue["get"] = defaultGet;
                    }
                    break;
                case "post" :
                    if("post" in existingPathVale){
                        pathValue["post"] = existingPathVale["post"];
                    } if(this.state.tmpMethods.indexOf("post") !== -1){
                        pathValue["post"] = defaultPost;
                    }
                    break;
                case "put" :
                    if("put" in existingPathVale){
                        pathValue["put"] = existingPathVale["put"];
                    } if(this.state.tmpMethods.indexOf("put") !== -1){
                        pathValue["put"] = defaultPost;
                    }
                    break;
                case "patch" :
                    if("patch" in existingPathVale){
                        pathValue["patch"] = existingPathVale["patch"];
                    } if(this.state.tmpMethods.indexOf("patch") !== -1){
                        pathValue["patch"] = defaultPost;
                    }
                    break;
                case "delete" :
                    if("delete" in existingPathVale){
                        pathValue["delete"] = existingPathVale["delete"];
                    } if(this.state.tmpMethods.indexOf("delete") !== -1){
                        pathValue["delete"] = defaultDelete;
                    }
                    break;
                case "head" :
                    if("head" in existingPathVale){
                        pathValue["head"] = existingPathVale["head"];
                    } if(this.state.tmpMethods.indexOf("head") !== -1){
                        pathValue["head"] = defaultHead;
                    }
                    break;
            }
        });


        tmpPaths[this.state.tmpResourceName] = pathValue;
        this.setState({paths:tmpPaths});
    }
    updatePath(path,method,value) {
        let tmpPaths = this.state.paths;
        if(value === null){
            delete tmpPaths[path][method];
        } else{
            tmpPaths[path][method] = value;
        }
        this.setState({paths:tmpPaths});
    }
    updateResources(){
        let tmpSwagger = this.state.swagger;
        tmpSwagger.paths = this.state.paths;
        this.setState({api:tmpSwagger});
        let promised_api = this.api.updateSwagger(this.api_uuid, this.state.swagger);
        promised_api.then((response) => {
            console.info(response);
        }).catch(error => {
            if (process.env.NODE_ENV !== "production")
                console.log(error);
            let status = error.status;
            if (status === 404) {
                this.setState({notFound: true});
            } else if (status === 401) {
                this.setState({isAuthorize: false});
                let params = qs.stringify({reference: this.props.location.pathname});
                this.props.history.push({pathname: "/login", search: params});
            }
        });
    }
    addRemoveToDeleteList(path,method){
        let pathDeleteList =  this.state.pathDeleteList;

        let deleteRef = {path: path, method: method};
        let itemAlreadyExisted = false;
        for(let i = 0; i < pathDeleteList.length; i++ ){
            if( pathDeleteList[i].path === path && pathDeleteList[i].method === method){
                pathDeleteList.splice(i,1);
                itemAlreadyExisted = true;
            }
        }

        if( !itemAlreadyExisted ){
            pathDeleteList.push(deleteRef);
        }
        this.setState({pathDeleteList: pathDeleteList});
    }
    handleCheckAll = event => {
        let paths = this.state.paths;
        let pathDeleteList = [];
        if (event.target.checked){
            for(let i=0; i < this.childResources.length; i++){
                if(this.childResources[i]){
                    this.childResources[i].toggleDeleteCheck(true);
                }
            }
            //We iterate all the paths and add each method and path to the pathDeleteList Object
            for(let path in paths) {
                if(paths.hasOwnProperty(path)){
                    if(Object.keys(path) && Object.keys(path).length > 0 ){
                        let pathValue = paths[path];
                        for(let method in pathValue ){
                            if(pathValue.hasOwnProperty(method)){
                                pathDeleteList.push({path: path,method: method});
                            }
                        }
                    } else {
                        console.debug("Error with path object");
                    }
                }
            }
            this.setState({allChecked:true});
            this.setState({pathDeleteList});
        } else {
            for(let i=0; i < this.childResources.length; i++){
                if(this.childResources[i]) {
                    this.childResources[i].toggleDeleteCheck(false);
                }
            }
            this.setState({allChecked:false});
            this.setState({pathDeleteList: []});
        }
    };
    deleteSelected = () => {
        let tmpPaths = this.state.paths;
        let pathDeleteList = this.state.pathDeleteList;
        for(let i = 0; i < pathDeleteList.length; i++){
            delete tmpPaths[pathDeleteList[i].path][pathDeleteList[i].method];
            let indexesToDelete = [];
            for(let j=0; j < this.childResources.length; j++){
                if(this.childResources[j] ) {
                    if(
                        this.childResources[j].props["path"] === pathDeleteList[i]["path"] &&
                        this.childResources[j].props["method"] === pathDeleteList[i]["method"]
                    ) {
                        indexesToDelete.push(j);
                    }
                }
            }
            for( let j=0; j < indexesToDelete.length; j++){
                this.childResources.splice(j, 1);   //Remove react child from reference array
            }
        }
        for(let i = 0; i < pathDeleteList.length; i++) {
            pathDeleteList.splice(i, 1); //Remove the item from waiting to be deleted list
        }


        this.setState({pathDeleteList: pathDeleteList});
        this.setState({path: tmpPaths});
        for(let i=0; i < this.childResources.length; i++){
            if(this.childResources[i]) {
                this.childResources[i].toggleDeleteCheck(false);
            }
        }
    }
    render(){
        if (!this.state.api) {
            return <Loading/>
        }
        const selectBefore = (
            <span>/SwaggerPetstore/1.0.0</span>
        );
        const plainOptions = ['get','post','put','delete','patch','head','options'];
        let paths = this.state.paths;
        const { classes } = this.props;
        return (

            <div className={classes.root}>
                <Typography type="display1" align="left" className={classes.mainTitle}>
                    Resources
                </Typography>
                <Grid container spacing={8}>
                    <Grid item md={12} lg={12}>
                        <Card >
                            <CardContent>
                                <Typography type="headline" component="h2">
                                    Add New Resource
                                </Typography>
                                <TextField
                                    id="tmpResourceName"
                                    label="URL Pattern"
                                    className={classes.textField}
                                    value={this.state.tmpResourceName}
                                    onChange={this.onChangeInput('tmpResourceName')}
                                    margin="normal"
                                />
                                <div style={{marginTop:20, display: 'flex', flexDirection: 'row'}}>
                                    { plainOptions.map( (option, index) =>  (
                                        <FormGroup key={index} row>

                                            <FormControlLabel
                                                control={
                                                    <Checkbox
                                                        checked={ this.state.tmpMethods.indexOf(option) > -1}
                                                        onChange={this.handleChange(option)}
                                                        value={option}
                                                    />
                                                }
                                                label={option.toUpperCase()}
                                            />
                                        </FormGroup>
                                    ))}
                                </div>
                                <Button raised className={classes.button}
                                        onClick={this.addResources}>Add Resources to Path</Button>

                            {this.state.apiScopes ? <div>
                                <Divider className={classes.divider} />
                                <FormControl className={classes.formControl}>
                                    <InputLabel htmlFor="select-multiple">Assign Global Scopes for API</InputLabel>
                                        <Select
                                            margin="none"
                                            multiple
                                            value={this.state.scopes}
                                            onChange={this.handleScopeChange}
                                            className={classes.scopes}>
                                            {this.state.apiScopes.list.map(tempScope => (
                                                <MenuItem
                                                    key={tempScope.name}
                                                    value={tempScope.name}
                                                    style={{
                                                        fontWeight: this.state.scopes.indexOf(tempScope.name) !== -1 ? '500' : '400',
                                                        width: 400
                                                    }}
                                                >
                                                    {tempScope.name}
                                                </MenuItem>
                                            ))}
                                        </Select>
                                </FormControl>
                            </div>: null}
                            </CardContent>
                        </Card>

                        <List>
                            {this.state.paths &&
                            <ListItem >
                                <FormControlLabel
                                    control={
                                        <Checkbox
                                            checked={this.state.allChecked}
                                            onChange={this.handleCheckAll}
                                            value=""
                                        />
                                    }
                                    label="Check All"
                                />
                                { Object.keys(this.state.pathDeleteList).length !== 0 &&
                                <ListItemSecondaryAction>

                                    <Button className={classes.button} raised color="secondary"
                                            onClick={this.deleteSelected}>
                                        Delete Selected
                                    </Button>

                                </ListItemSecondaryAction> }

                            </ListItem>
                            }
                        {
                            Object.keys(paths).map(
                                (key) => {
                                    let path = paths[key];
                                    let that = this;
                                    return (
                                        Object.keys(path).map( (innerKey) => {
                                            return <Resource path={key} method={innerKey} methodData={path[innerKey]}
                                                             updatePath={that.updatePath}
                                                             apiScopes={this.state.apiScopes}
                                                             addRemoveToDeleteList={that.addRemoveToDeleteList}
                                                             onRef={ ref => this.childResources.push(ref) }
                                            />
                                        })
                                    );


                                }
                            )
                        }
                        </List>
                        <ApiPermissionValidation userPermissions={this.state.api.userPermissionsForApi}>
                            <Button raised color="primary"
                                    onClick={this.updateResources}
                                    className={classes.button}>
                                Save
                            </Button>
                        </ApiPermissionValidation>
                    </Grid>
                </Grid>

            </div>
        )
    }
}
Resources.propTypes = {
    classes: PropTypes.object.isRequired,
};

<<<<<<< HEAD
export default withStyles(styles)(Resources);
=======
export default withStyles(styles)(Resources);
>>>>>>> 6479ab26681e732f5125f7b85f58c96e8632053c
