import React, { useEffect } from 'react'
import { useState } from 'react'
import axios from 'axios'
import { useDispatch } from 'react-redux';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { fetchUser } from '../store/userInfoSlice';
import Cookies from 'js-cookie';

function AddDevice() {
    const Server_IP = process.env.REACT_APP_Server_IP;
    const dispatch = useDispatch();
    const [deviceNumber, setDeviceNumber] = useState("");
    const [deviceName, setDeviceName] = useState("");
    const [isInputFocused, setIsInputFocused] = useState(false);
    const [isChecked, setIsChecked] = useState(false);

    useEffect(() => {
        if (isInputFocused === false && deviceNumber) {
            checkSerialNumber(deviceNumber)
        }
    }, [isInputFocused])

    const handleInputFocus = () => {
        setIsInputFocused(true);
    };

    const handleInputBlur = () => {
        setIsInputFocused(false);
    };

    const checkSerialNumber = async (deviceNumber) => {
        await axios.post(`${Server_IP}/kit/validate`, { serialNum: deviceNumber },
            {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${Cookies.get("accessToken")}`
                },
            })
            .then((res) => {
                if (res.data) {
                    alert("This kit is already registered")
                    setDeviceNumber('')
                } else {
                    alert("This Kit is possible to register")
                    setIsChecked(true);
                }
            })
            .catch((error) => {
                alert("This serial Number is invalid");
                setDeviceNumber('')
            })
    };

    const handleDeviceNumber = () => {
        const accessToken = Cookies.get('accessToken');
        registerDeviceNumber(deviceNumber, deviceName, accessToken)
        dispatch(fetchUser());
        window.location.reload();
    }

    const registerDeviceNumber = async (deviceNumber, deviceName , accessToken) => {
        await axios.post(`${Server_IP}/kit`, { serialNum: deviceNumber, deviceName: deviceName },
        {
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${accessToken}`
            },
        })
        .then((res) => {
            if (res.status === 204) {
                alert("중복");
            } else {
                alert("Success")
            }
        })
        .catch((error) => {
            alert("Server Error");
        })
    }


    return (
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
            <button style={{
                backgroundColor: '#EEEEFF',
                color: "black",
                borderRadius: '50px',
                position: 'fixed',
                width: '100px',
                height: '40px',
                bottom: '30px',
                marginRight: '10px',
                fontSize: '15px',
                fontWeight: 'bold',
                zIndex: '500',
            }} type="button" className="btn" data-bs-toggle="modal" data-bs-target="#addDevice" data-bs-whatever="@getbootstrap"><FontAwesomeIcon icon={faPlus} /> Device</button>
            <div className="modal fade" id="addDevice" tabindex="-1" aria-labelledby="findModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content" style={{ height: '600px'  }}>
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="exampleModalLabel">Add Device</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body" style={{ fontSize: '15px' }}>
                            <form>
                                <div className="mb-3">
                                    <label htmlFor="recipient-name" className="col-form-label" >Device SerialNumber</label>
                                    <input type="text" className="form-control" id="recipient-name" onFocus={handleInputFocus}
                                        onBlur={handleInputBlur} value={deviceNumber} onChange={(e) => setDeviceNumber(e.target.value)} />
                                    <label htmlFor="recipient-name" className="col-form-label" >Device Name</label>
                                <input type="text" className="form-control" id="recipient-name" value={deviceName} onChange={(e) => setDeviceName(e.target.value)} />
                                </div>
                                <div style={{ fontSize: '20px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}> 
                                    <span style={{ color: 'orange' }}>1. Connect Wifi</span>
                                    <span style={{ color: '#e2bd15', paddingTop: '10px' }}>2. Please enter the address and register for Wi-Fi</span>
                                </div>
                                <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', scale: '200%', marginTop: '70px' }}>
                                    <img src='/DeviceWifi.png' width='90px' height='60px' />
                                </div>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button style={{ backgroundColor: '#73BD72', color: 'white' }} type="button" className="btn" data-bs-dismiss="modal" onClick={handleDeviceNumber} disabled={!isChecked || !deviceName}>Add</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}




export default AddDevice