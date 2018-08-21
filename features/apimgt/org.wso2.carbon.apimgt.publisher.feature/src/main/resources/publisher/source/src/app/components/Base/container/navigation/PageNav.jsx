import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';

import Divider from '@material-ui/core/Divider';

const styles = theme => ({
    root: {
        flexGrow: 1,
        height: '100%',
        overflow: 'hidden',
        position: 'fixed',
        display: 'flex',
        zIndex: 100,
    },
    drawerPaper: {
        position: 'relative',
        width: 'calc(100vw*1/12)', // This is because of the container grid sizing (navbar md2 content md10 = 2/12)
        [theme.breakpoints.down('md')]: {
            width: 'calc(100vw*2/12)',
        },
        [theme.breakpoints.down('sm')]: {
            width: 'calc(100vw*1/12)',
        },
        [theme.breakpoints.down('xs')]: {
            width: 'calc(100vw*2/12)',
        }, // This is because of the container grid sizing when sm (navbar sm1 content sm11 = 1/12)
        backgroundColor: theme.palette.background.navBar,
    },
});

const PageNav = (props) => {
    const { classes, section, navItems } = props;
    return (
        <div className={classes.root}>
            <Drawer
                variant='permanent'
                classes={{
                    paper: classes.drawerPaper,
                }}
            >
                <div className={classes.toolbar} />
                <List>{section}</List>
                <Divider />
                <List>{navItems}</List>
            </Drawer>
        </div>
    );
};

PageNav.propTypes = {
    classes: PropTypes.shape({}).isRequired,
    section: PropTypes.element.isRequired,
    navItems: PropTypes.arrayOf(PropTypes.element).isRequired,
};

export default withStyles(styles)(PageNav);
