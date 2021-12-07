import * as React from 'react';
import Title from '../components/Title';
import DirectoryForm from './DirectoryForm';


export default function IntentIndex() {
  return (
    <React.Fragment>
      <Title>Directories</Title>
      <DirectoryForm></DirectoryForm>
    </React.Fragment>
  );
}
