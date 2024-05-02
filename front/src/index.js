import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import store from './store/store';
import { Provider } from 'react-redux';
import { CookiesProvider } from 'react-cookie';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <CookiesProvider>
    <Provider store = { store }>
      <React.StrictMode>
      <div
        className='body'
        style={{
          position: "absolute",
          top: "0",
          left: "0",
          width: "100%",
          minHeight: "100vh",
          backgroundColor: "rgba(244, 250, 235, 0.8)",
        }}
      >
        <App />
      </div>
      </React.StrictMode>
    </Provider >
  </CookiesProvider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
