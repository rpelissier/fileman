import React from "react";
import {Button, Form, InputGroup} from "react-bootstrap";
import './DirectoryForm.css'

type Props = {}

type State = {
    directory: string;
};

class DirectoryForm extends React.Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {directory: ''};
    }

    //TODO binder sur le Input du form onChange={this.onChange}
    onChange = (e: React.FormEvent<HTMLInputElement>): void => {
        this.setState({directory: e.currentTarget.value});
    };

    onSubmit = (e: React.FormEvent<HTMLButtonElement>): void => {
        console.log("Let's call the API with "+this.state.directory)
    };

    render() {
        return (
            <div className="DirectoryForm">
                <Form>
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>Directory</Form.Label>
                        <Form.Control type="text" placeholder="/Users/renaud/Downloads"/>
                        <Form.Text className="text-muted">
                            This directory will be scanned.
                        </Form.Text>
                    </Form.Group>
                    <Button type="submit" onSubmit={this.onSubmit}>Submit</Button>
                </Form>
            </div>
        );
    }
}

export default DirectoryForm;