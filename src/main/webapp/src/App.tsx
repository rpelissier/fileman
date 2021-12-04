import React from 'react';
import logo from './logo.svg';
import './App.css';
import DirectoryForm from "./DirectoryForm";
import { Navbar, Container, NavDropdown, Nav, Row, Col } from 'react-bootstrap';

function App() {
    return (
        <div className="App">
            <Navbar bg="light" expand="lg">
                <Container>
                    <Navbar.Brand href="#home">React-Bootstrap</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link href="#home">Home</Nav.Link>
                            <Nav.Link href="#link">Link</Nav.Link>
                            <NavDropdown title="Dropdown" id="basic-nav-dropdown">
                                <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
                                <NavDropdown.Item href="#action/3.2">Another action</NavDropdown.Item>
                                <NavDropdown.Item href="#action/3.3">Something</NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item href="#action/3.4">Separated link</NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <Container fluid>
                <Row className="content">
                    <Col sm={3} className="sidenav">Side nav</Col>
                    <Col sm={9} className="center">
                        <DirectoryForm></DirectoryForm>
                    </Col>
                </Row>
            </Container>
            <Container fluid className="footer">
                Footer
            </Container>

        </div>
    );
}

export default App;
