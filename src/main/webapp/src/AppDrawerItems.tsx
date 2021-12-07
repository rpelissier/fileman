import * as React from 'react';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import SearchSharpIcon from '@mui/icons-material/SearchSharp';
import SensorsSharpIcon from '@mui/icons-material/SensorsSharp';
import ContentCopySharpIcon from '@mui/icons-material/ContentCopySharp';

//https://mui.com/components/material-icons/

export const MainListItems = (
  <div>
    <ListItem button>
      <ListItemIcon>
        <SensorsSharpIcon />
      </ListItemIcon>
      <ListItemText primary="Index" />
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <SearchSharpIcon />
      </ListItemIcon>
      <ListItemText primary="Search" />
    </ListItem>
    <ListItem button>
      <ListItemIcon>
        <ContentCopySharpIcon />
      </ListItemIcon>
      <ListItemText primary="Duplicates" />
    </ListItem>
  </div>
);
