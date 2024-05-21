// LoginPage.jsx
import React, { useEffect } from 'react';
import './loginPage.css'; // CSS 파일을 import
import { useDispatch } from 'react-redux';
import axios from 'axios';
import { useState, useRef } from 'react';
import SignUp from './SignUp';
import FindId from './FindId';
import FindPassword from './FindPassword';
import { useCookies } from 'react-cookie';
import Cookies from 'js-cookie';
import { useNavigate } from 'react-router-dom';
import { useFetch } from '../hooks/useFetch';
import { useAccessToken } from '../contexts/AccessTokenContext';
import { useIsLogin } from '../contexts/IsLoginContext';

export default function LoginPage() {
    const Server_IP = process.env.REACT_APP_Server_IP;
    const [userId, setUserId] = useState("");
    const [password, setPassword] = useState("");
    const [cookies, setCookie, removeCookie] = useCookies(['accessToken']);
    const focusRef = useRef();
    const navigate = useNavigate();
    const { statusCode: loginStatusCode, postReq: loginPost, response: loginResponse } = useFetch();
    const { accessToken, setAccessToken } = useAccessToken();
    const { isLogin, setIsLogin } = useIsLogin();

    useEffect(() => {
        focusRef.current.focus()
    }, [])

    const handleIdChange = (e) => {
        setUserId(e.target.value);
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    const handleLogin = () => {
        loginRequest(userId, password);
    };

    const handleOnKeyPress = (e) => {
        if (e.key === 'Enter') {
            handleLogin();
        }
    }

    const loginRequest = async (userId, password) => {
        await loginPost({
            url: 'auth/login',
            data: { userId: userId, userPassword: password },
            token: false,
        })
    }

    useEffect(() => {
        if (loginResponse) {
            if (loginStatusCode === 200) {
                setAccessToken(loginResponse.accessToken)
                navigate('/')
            } else {
                alert("Please check your Id and Password.");
            }
        }
    }, [loginResponse])

    return (
        <>
            <h2 className="login-title">Green Buddy</h2>
            <div className="center-container" style={{ display: 'flex', flexDirection: 'column' }}> 
                <div className="login-container">
                    <input type="text" ref={ focusRef } placeholder="ID" className="input-field" value={ userId } onChange={ handleIdChange } />
                    <input type="password" placeholder="Password" className="input-field" value={password} onChange={handlePasswordChange} onKeyPress={handleOnKeyPress} />
                    <button type="button" className="login-button" style={{ width: '100%' }}  onClick={() => handleLogin() }>Login</button>
                </div>
                <div className='additional-container' style={{ marginTop: '50px', display: 'flex', justifyContent: 'space-between'  }}>
                    <FindId style={{ marginRight: '10px' }}></FindId>
                    <FindPassword style={{ marginRight: '10px' }}></FindPassword>
                    <SignUp></SignUp>
                </div>
            </div>
        </>
    );

}