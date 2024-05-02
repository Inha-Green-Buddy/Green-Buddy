import React from 'react';
import { Navbar, Nav, NavDropdown } from 'react-bootstrap';
import './navUpper.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHouse, faFileLines, faSeedling, faDroplet, faUser, faBars, faGear, faMagnifyingGlass, faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom';
import NavUserInfo from './NavUserInfo';
import { useSelector } from 'react-redux';

function NavUpper() {

    const userNickname = useSelector(state => state.userInfo.value.infos.userNickname)

    return (
        <div style={{ color: '#291528', margin: '6px 12px', display: 'flex', justifyContent: 'center' }} >
            <NavUserInfo></NavUserInfo>

            <Navbar
                className='col-11 col-lg-10'
                style={{ backgroundColor: '#EEEEFF', marginTop: '8px', borderRadius: '15px', display: 'flex', alignContent: 'center', fontSize: '16px', paddingRight: '20px' }}
                expand="lg"
            >
                <Link to="/home" style={{ color: '#291528', textDecoration: 'none' }} >
                    <Navbar.Brand className='nav-title' style={{ color: '#291528', marginLeft: '20px' }}>  
                    <img style={{ width: '50px' }} src='/icon.png' />
                </Navbar.Brand>
                </Link>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
                    <Nav style={{ color: '#291528', letterSpacing: '2px' }}>
                        <Nav.Link as={Link} to="/home" className="nav-link-item">
                            <FontAwesomeIcon icon={faHouse} style={{ color: '#291528', paddingTop: '11px' }} /> Home
                        </Nav.Link>
                        <Nav.Link className="nav-link-item" as={Link} to="/search"><FontAwesomeIcon icon={faMagnifyingGlass} style={{ color: '#291528', paddingTop: '11px' }} /> Search Plant</Nav.Link>
                        <Nav.Link className="nav-link-item" as={Link} to="/status"><FontAwesomeIcon icon={faSeedling} style={{ color: '#291528', paddingTop: '11px' }} /> Bloom Diary</Nav.Link>
                        <div data-bs-toggle="offcanvas" data-bs-target="#offcanvasRight" aria-controls="offcanvasRight">
                            <Nav.Link className="nav-link-item" style={{ marginLeft: '7px', fontWeight: '900', backgroundColor: 'none' }}>
                                <button className="btn" type="button" style={{ border: 0, backgroundColor: 'transparent', margin: 0, marginLeft: '2px', color: '#291528', letterSpacing: '2px' }}><FontAwesomeIcon icon={faUser} style={{ color: '#291528', fontSize: '16px' }} />{userNickname ? ` ${userNickname}` : 'Please try to login'}</button>
                            </Nav.Link>
                        </div>
                        <NavDropdown title="Dropdown" id="basic-nav-dropdown" style={{ display: 'none', marginRight: '10px' }}>
                        </NavDropdown>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        </div>
    )
};

export default NavUpper;