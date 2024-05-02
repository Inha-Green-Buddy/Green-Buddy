// eslint-disable-next-line no-restricted-globals

import './App.css';
import LoginPage from './login/LoginPage';
import NavUpper from './navUpper/NavUpper';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomeContent from './homeContent/HomeContent';
import PlantStatusContent from './plantStatusContent/PlantStatusContent';
import { Navigate, Outlet } from 'react-router-dom';
import { fetchUser, refreshUserInfo } from './store/userInfoSlice';
import Cookies from 'js-cookie';
import { useDispatch } from 'react-redux';
import { useEffect, useState } from 'react';
import SearchContent from './searchContent/SearchContent';

const checkLogin = () => {
  if (Cookies.get("accessToken")) {
    return true
  } else {
    return false
  }
}

function App() {
  
  const dispatch = useDispatch()
  let isLogin = checkLogin();
  
  if (isLogin) {
    dispatch(fetchUser());
  }

  useEffect(() => {
    const intervalCheck = setInterval(() => {
      if (isLogin && !checkLogin()) { window.location.reload(); }
    }, 10000);

    return () => clearInterval(intervalCheck)
  }, []);

  return (
    <div className='App col-12 col-lg-10' style={{ margin: '0 auto' }}>
      <Router>
        {isLogin && <NavUpper />}
        <Routes>
          <Route path="/home" element={isLogin ? <HomeContent /> : <Navigate to="/login" />}/>
          <Route path="/search" element={isLogin ? <SearchContent /> : <Navigate to="/login" />} />
          <Route path="/status" element={isLogin ? <PlantStatusContent /> : <Navigate to="/login" />} />
          <Route path="/login" element={isLogin ? <Navigate to="/home" /> : <LoginPage />} />
          <Route path="*" element={isLogin ? <Navigate to="/home" /> : <Navigate to="/login" />}></Route>
        </Routes>
      </Router>
    </div>
  );
}

export default App;