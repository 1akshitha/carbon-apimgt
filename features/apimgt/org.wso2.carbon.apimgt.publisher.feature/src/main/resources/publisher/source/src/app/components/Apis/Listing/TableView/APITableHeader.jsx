import React from 'react';
import PropTypes from 'prop-types';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Tooltip from '@material-ui/core/Tooltip';

const rows = [
    {
        id: 'name',
        numeric: false,
        disablePadding: true,
        label: 'Name',
    },
    {
        id: 'version',
        numeric: true,
        disablePadding: false,
        label: 'Version',
    },
    {
        id: 'context',
        numeric: true,
        disablePadding: false,
        label: 'Context',
    },
    {
        id: 'subscriptions',
        numeric: true,
        disablePadding: false,
        label: 'Subscriptions',
    },
    {
        id: 'provider',
        numeric: true,
        disablePadding: false,
        label: 'provider',
    },
];

export default class APITableHeader extends React.Component {
    constructor(props) {
        super(props);
        this.createSortHandler = this.createSortHandler.bind(this);
    }

    createSortHandler(property) {
        return (event) => {
            this.props.onRequestSort(event, property);
        };
    }

    render() {
        const {
            onSelectAllClick, order, orderBy, numSelected, rowCount,
        } = this.props;

        return (
            <TableHead>
                <TableRow>
                    <TableCell padding='checkbox'>
                        <Checkbox
                            indeterminate={numSelected > 0 && numSelected < rowCount}
                            checked={numSelected === rowCount}
                            onChange={onSelectAllClick}
                        />
                    </TableCell>
                    {rows.map((row) => {
                        return (
                            <TableCell
                                key={row.id}
                                numeric={row.numeric}
                                padding={row.disablePadding ? 'none' : 'default'}
                                sortDirection={orderBy === row.id ? order : false}
                            >
                                <Tooltip
                                    title='Sort'
                                    placement={row.numeric ? 'bottom-end' : 'bottom-start'}
                                    enterDelay={300}
                                >
                                    <TableSortLabel
                                        active={orderBy === row.id}
                                        direction={order}
                                        onClick={this.createSortHandler(row.id)}
                                    >
                                        {row.label}
                                    </TableSortLabel>
                                </Tooltip>
                            </TableCell>
                        );
                    }, this)}
                </TableRow>
            </TableHead>
        );
    }
}

APITableHeader.propTypes = {
    numSelected: PropTypes.number.isRequired,
    onRequestSort: PropTypes.func.isRequired,
    onSelectAllClick: PropTypes.func.isRequired,
    order: PropTypes.string.isRequired,
    orderBy: PropTypes.string.isRequired,
    rowCount: PropTypes.number.isRequired,
};
