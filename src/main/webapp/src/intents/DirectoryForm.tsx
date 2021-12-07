import { FormControl, FormHelperText, Input, InputLabel } from "@mui/material";
import React from "react";

export default function DirectoryForm() {
    return (
        <React.Fragment>
            <FormControl>
                <InputLabel htmlFor="my-input">Directory</InputLabel>
                <Input id="my-input" aria-describedby="my-helper-text" />
                <FormHelperText id="my-helper-text">This directory will be scanned.</FormHelperText>
            </FormControl>
        </React.Fragment>
    );
}