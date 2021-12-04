import React from "react";
import { Button, Form } from "react-bootstrap";
import './DirectoryForm.css'

function DirectoryForm() {
    const [dir, setDir] = React.useState('')

    const onDirChange = (e: React.FormEvent<HTMLInputElement>) => {
        setDir(e.currentTarget.value)
    };

    const onSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        alert(`Selected dir : '${dir}'.`)
    };

    return (
        <div className="DirectoryForm">
            <Form onSubmit={onSubmit}>
                <Form.Group controlId="formBasicEmail">
                    <Form.Label>Directory</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="/Users/renaud/Downloads"
                        onInput={onDirChange}
                    />
                    <Form.Text>This directory will be scanned.</Form.Text>
                </Form.Group>
                <Button type="submit">Submit</Button>
            </Form>
        </div>
    );

}

export default DirectoryForm;