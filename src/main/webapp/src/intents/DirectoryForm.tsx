import { Label } from "@mui/icons-material";
import { Avatar, Button, FormControl, FormHelperText, Input, InputLabel, List, ListItem, ListItemAvatar, ListItemText } from "@mui/material";
import ImageIcon from '@mui/icons-material/Image';
import React from "react";

import { listDirs, loadDir } from "../service/SourceDirectoryService";

export default function DirectoryForm() {

    const [dir, setDir] = React.useState('');
    const [result, setResult] = React.useState('');
    const [directories, setDirectories] = React.useState([] as string[]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setDir(event.target.value);
    };

    const refreshDir = () => {
        console.log(`Loading ${dir}`);
        loadDir(dir)
            .then((it) => {
                setResult(it);
                refreshDirectories();
            })
            .catch((it) => setResult(it));
    };

    const refreshDirectories = () => {
        listDirs().then((it) =>
            setDirectories(it)
        );
    }

    function listItem(path: string) {
        return <ListItem key={path}>
            <ListItemAvatar><Avatar><ImageIcon /></Avatar></ListItemAvatar>
            <ListItemText primary={path} secondary="Directory" />
        </ListItem>
    }

    React.useEffect(refreshDirectories, []);

    return (
        <React.Fragment>
            <FormControl>
                <InputLabel htmlFor="my-input">Directory</InputLabel>
                <Input id="my-input" aria-describedby="my-helper-text" onChange={handleChange} />
                <FormHelperText id="my-helper-text">This directory will be scanned.</FormHelperText>
            </FormControl>
            <Label>{result}</Label>
            <Button onClick={refreshDir}>Scanner</Button>
            <List sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}>
                {directories.map((it) => listItem(it))}
            </List>
        </React.Fragment>
    );
}