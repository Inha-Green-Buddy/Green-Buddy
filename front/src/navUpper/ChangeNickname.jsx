import React from 'react'
import { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import { MdDriveFileRenameOutline } from 'react-icons/md'
import { useSelector } from 'react-redux';


function ChangeNickname({ exUserNickname }) {

    const Server_IP = process.env.REACT_APP_Server_IP;
    const accessToken = Cookies.get('accessToken');
    const [modalVisible, setModalVisible] = useState(false);
    const userId = useSelector(state => state.userInfo.value.infos.userId)


    const [form, setForm] = useState({ newNickname: '' })

    const handleChangePassword = () => {
        requestChangeNickname(form.newNickname, userId)
    }

    const requestChangeNickname = async (newNickname, userId) => {
        await axios.post(`${Server_IP}/users/nickname`, { userId: userId, userNickname: newNickname },
            {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${accessToken}`
                },
            })
            .then((res) => {
                alert("Complete")
                window.location.reload();
            })
            .catch((error) => {
                alert(error);
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
                }} onClick={() => setModalVisible(!modalVisible)}><MdDriveFileRenameOutline /> Change Nickname</button>

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
                        <div className='changeNicknameContent'>
                            <h2> Change Nickname</h2><br />
                            <div style={{ marginBottom: '20px', fontSize: '16px' }}>Current Nickname : {exUserNickname}</div>
                            <input type="text" placeholder="New Nickname" className="input-field" value={form.newNickname} onChange={(e) => setForm({ ...form, newNickname: e.target.value })} />
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

export default ChangeNickname
