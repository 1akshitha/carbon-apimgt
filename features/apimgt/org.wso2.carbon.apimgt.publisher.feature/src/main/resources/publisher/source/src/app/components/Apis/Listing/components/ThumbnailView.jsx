import React, { Component } from 'react';
import { ButtonBase, Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography, Slide } from '@material-ui/core';
import { withStyles } from '@material-ui/core/styles';
import Alert from 'AppComponents/Shared/Alert';
import Api from 'AppData/api';
import CancelIcon from '@material-ui/icons/Cancel';
import Card from '@material-ui/core/Card';
import CardMedia from '@material-ui/core/CardMedia';
import Dropzone from 'react-dropzone';
import EditIcon from '@material-ui/icons/Edit';
import PropTypes from 'prop-types';
import UploadIcon from '@material-ui/icons/Send';
import green from '@material-ui/core/colors/green';
import red from '@material-ui/core/colors/red';

import ImageGenerator from './ImageGenerator';

const styles = theme => ({
    acceptDrop: {
        backgroundColor: green[50],
    },
    card: {
        maxWidth: 80,
    },
    dropzone: {
        border: '2px dashed rgb(102, 102, 102)',
        borderRadius: '5px',
        cursor: 'pointer',
        height: theme.spacing.unit * 20,
        padding: `${theme.spacing.unit * 2}px 0px`,
        position: 'relative',
        textAlign: 'center',
        width: '100%',
    },
    rejectDrop: {
        backgroundColor: red[50],
    },
    thumb: {
        '&:hover': {
            zIndex: 1,
            '& $thumbBackdrop': {
                opacity: 0.2,
            },
        },
    },
    thumbBackdrop: {
        position: 'absolute',
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
        backgroundColor: theme.palette.common.black,
        opacity: 0.4,
    },
    thumbButton: {
        position: 'absolute',
        left: 0,
        right: 0,
        top: 0,
        bottom: 0,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        color: theme.palette.common.white,
    },
    media: {
        // ⚠️ object-fit is not supported by IE11.
        objectFit: 'cover',
    },
    paper: {
        position: 'absolute',
        width: theme.spacing.unit * 100,
        backgroundColor: theme.palette.background.paper,
        boxShadow: theme.shadows[5],
        padding: theme.spacing.unit * 4,
        top: '25%',
        left: `calc(50% - ${(theme.spacing.unit * 100) / 2}px)`,
    },
    preview: {
        height: theme.spacing.unit * 20,
        width: theme.spacing.unit * 20,
    },
});

/**
 * Slide up transition for modal
 * @param {any} props Properties
 * @returns {Slide} Slide up transition
 */
function Transition(props) {
    return <Slide direction='up' {...props} />;
}

/**
 * Provides a view for the API Thumbnail image.
 * This can be either user defined image uploaded earlier or a generated Image.
 */
class ThumbnailView extends Component {
    /**
     * Initializes the ThumbnailView Component
     * @param {any} props Component properties
     */
    constructor(props) {
        super(props);
        this.state = { open: false, file: null };
        this.handleClick = this.handleClick.bind(this);
        this.handleClose = this.handleClose.bind(this);
    }
    /**
     * Event listener for file drop on the dropzone
     * @param {File} acceptedFile dropeed file
     */
    onDrop(acceptedFile) {
        this.setState({ file: acceptedFile });
    }
    /**
     * @param {SyntheticEvent} e React event object
     */
    handleClick(e) {
        if (e.target.id === 'btnEditAPIThumb') {
            this.setState({ open: true });
        } else if (e.target.id === 'btnUploadAPIThumb') {
            const { api } = this.props;
            this.uploadThumbnail(api.id, this.state.file[0]);
        }
    }
    /**
     * Add new thumbnail image to an API
     *
     * @param {String} apiId ID of the API to be updated
     * @param {File} file new thumbnail image file
     */
    uploadThumbnail(apiId, file) {
        if (!apiId || !file) {
            Alert.error('Invalid file or API information is not set correctly.');
            return;
        }
        const api = new Api();
        const thumbnailPromise = api.addAPIThumbnail(apiId, file);
        thumbnailPromise.then((response) => {
            Alert.info('Thumbnail uploaded successfully');
        }).catch((error) => {
            if (process.env.NODE_ENV !== 'production') {
                console.log(error);
            }
            Alert.error('Error occured while uploading new thumbnail. Please try again.');
        });
    }
    /**
     * Handle modal close event
     */
    handleClose() {
        if (this.state.file) {
            window.URL.revokeObjectURL(this.state.file.preview);
        }

        this.setState({ open: false, file: null });
    }
    /**
     * @inheritdoc
     */
    render() {
        const { api, classes } = this.props;
        const { file } = this.state;
        let view;

        if (api.thumb) {
            view = (
                <Card className={classes.card}>
                    <CardMedia
                        component='img'
                        className={classes.media}
                        height={80}
                        image={api.thumb}
                        title='API Thumbnail'
                    />
                </Card>
            );
        } else {
            view = <ImageGenerator width={80} height={80} api={api} />;
        }

        return (
            <div>
                <ButtonBase
                    focusRipple
                    className={classes.thumb}
                    onClick={this.handleClick}
                    id='btnEditAPIThumb'
                >
                    {view}
                    <span className={classes.thumbBackdrop} />
                    <span className={classes.thumbButton}>
                        <Typography
                            component='span'
                            variant='subheading'
                            color='inherit'
                        >
                            <EditIcon />
                        </Typography>
                    </span>
                </ButtonBase>

                <Dialog
                    TransitionComponent={Transition}
                    aria-labelledby='thumb-dialog-title'
                    disableBackdropClick
                    open={this.state.open}
                    onClose={this.handleClose}
                    fullWidth='true'
                    maxWidth='lg'
                >
                    <DialogTitle id='thumb-dialog-title'>Upload Thumbnail</DialogTitle>
                    <DialogContent>
                        <Dropzone
                            multiple={false}
                            accept='image/*'
                            className={classes.dropzone}
                            activeClassName={classes.acceptDrop}
                            rejectClassName={classes.rejectDrop}
                            onDrop={(dropFile) => { this.onDrop(dropFile); }}
                        >
                            <Typography
                                component='span'
                                variant='title'
                                color='inherit'
                            >
                                Drop your image or click the box to upload image
                            </Typography>
                        </Dropzone>
                        {file && file.length > 0 &&
                            <Card>
                                <CardMedia className={classes.media}>
                                    <img className={classes.preview} src={file[0].preview} alt='Thumbnail Preview' />
                                </CardMedia>
                            </Card>
                        }
                    </DialogContent>
                    <DialogActions>
                        <Button variant='outlined' color='primary' onClick={this.handleClick} id='btnUploadAPIThumb' >
                            <UploadIcon />
                            Upload
                        </Button>
                        <Button variant='outlined' onClick={this.handleClose} color='secondary'>
                            <CancelIcon />
                            Cancel
                        </Button>
                    </DialogActions>
                </Dialog>
            </div>
        );
    }
}

ThumbnailView.propTypes = {
    api: PropTypes.shape({}).isRequired,
    classes: PropTypes.shape({}).isRequired,
};

export default withStyles(styles)(ThumbnailView);
