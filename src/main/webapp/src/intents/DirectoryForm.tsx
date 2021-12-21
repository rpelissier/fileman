import { Label } from "@mui/icons-material";
import { Button, FormControl, FormHelperText, Input, InputLabel } from "@mui/material";
import React from "react";

import { loadDir } from "../service/SourceDirectoryService";

export default function DirectoryForm() {

    const [dir, setDir] = React.useState('');
    const [result, setResult] = React.useState('');

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setDir(event.target.value);
    };

    const handleClick = () => {
        console.log(`Loading ${dir}`)
        loadDir(dir)
            .then((it) => setResult(it))
            .catch((it) => setResult(it))
    };

    return (
        <React.Fragment>
            <FormControl>
                <InputLabel htmlFor="my-input">Directory</InputLabel>
                <Input id="my-input" aria-describedby="my-helper-text" onChange={handleChange} />
                <FormHelperText id="my-helper-text">This directory will be scanned.</FormHelperText>
            </FormControl>
            <Label>{result}</Label>
            <Button onClick={handleClick}>Scanner</Button>
        </React.Fragment>
    );
}