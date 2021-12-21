import React from "react";
import { Grid } from "@mui/material"
import Paper from '@mui/material/Paper';
import Deposits from './intents/IntentDeposits';
import IntentIndex from "./intents/IntentIndex";
import Orders from './intents/IntentOrders';



export default function AppBody() {
    return (
        <React.Fragment>
            <Grid item xs={12} md={8} lg={6}>
                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }}>
                    <IntentIndex />
                </Paper>
            </Grid>
            {/* Recent Deposits */}
            <Grid item xs={12} md={4} lg={3}>
                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240, }}>
                    <Deposits />
                </Paper>
            </Grid>
            {/* Recent Orders */}
            <Grid item xs={12}>
                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                    <Orders />
                </Paper>
            </Grid>
        </React.Fragment>
    )
}