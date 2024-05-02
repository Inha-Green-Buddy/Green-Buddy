import React from 'react'
import { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import { RiLockPasswordFill } from 'react-icons/ri'

function ChangePassword() {

    const Server_IP = process.env.REACT_APP_Server_IP;
    const accessToken = Cookies.get('accessToken');
    const [modalVisible, setModalVisible] = useState(false);

    const [form, setForm] = useState({ exPassword: '', newPassword: '' })

    const handleChangePassword = () => {
      requestChangePassword(form.exPassword, form.newPassword)
    }

    const requestChangePassword = async (exPassword, newPassword) => {
      await axios.post(`${Server_IP}/users/password`, { exPassword: exPassword, newPassword: newPassword },
        {
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${accessToken}`
          },
        })
        .then((res) => {
          alert("Success.")
          Cookies.remove("accessToken");
          window.location.reload();
        })
        .catch((error) => {
          alert(error)
        })
    }

  return (
    <div>
      <div>
        <button type="button" className='btn hover-button' style={{
           color: 'black',
           paddingLeft:
           '7px',
           paddingRight: '7px',
           width: '100%',
           fontSize: '18px',
        }} onClick={() => setModalVisible(!modalVisible)}><RiLockPasswordFill /> Change Password</button>

              {modalVisible && (
                <div style={{
                  position: 'fixed',
                  top: '50%',
                  left: '50%',
                  transform: 'translate(-50%, -50%)',
                  backgroundColor: '#fff',
                  padding: '50px',
                  zIndex: '1000',
                  borderRadius: '10px',
                  minWidth: '300px',
                }}>
                    <div className='changePasswordContent'>
              <h2> Change Password</h2><br />
              <input type="password" placeholder="Current Password" className="input-field" value={form.exPassword} onChange={(e) => setForm({ ...form, exPassword: e.target.value })} />
              <input type="password" placeholder="Change Password" className="input-field" value={form.newPassword} onChange={(e) => setForm({ ...form, newPassword: e.target.value })} />
              <div style={{ display: 'flex', justifyContent: 'right' }}>
                <button type="button" onClick={handleChangePassword} style={{ color: 'white', backgroundColor: '#73BD72', paddingLeft: '7px', paddingRight: '7px' }} className="btn">Submit</button>
              </div>
                    </div>
                </div>
              )}

              {modalVisible && <div style={{
                position: 'fixed',
                top: '0',
                left: '0',
                width: '100vw',
                height: '100vh',
                backgroundColor: 'rgba(0, 0, 0, .5)',
                zIndex: '500'
              }} onClick={() => setModalVisible(false)} />}
            </div>

    </div>
  )
}

export default ChangePassword
