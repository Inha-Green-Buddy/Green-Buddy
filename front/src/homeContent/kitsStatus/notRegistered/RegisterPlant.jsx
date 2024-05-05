import React from 'react'
import { useState, useRef } from 'react'
import axios from 'axios'
import { useDispatch } from 'react-redux'
import Cookies from 'js-cookie'
import { fetchUser } from '../../../store/userInfoSlice'
import './registerPlant.css'
import ToolTip from '../../../component/Tooltip'

function RegisterPlant({kitNo}) {

    const Server_IP = process.env.REACT_APP_Server_IP;
    const AI_IP = process.env.REACT_APP_AI_IP;

    const fileInputRef = useRef();

    const accessToken = Cookies.get('accessToken');
    const dispatch = useDispatch();

    const [isShowPossibility, setIsShowPossibility] = useState(false);
    const [possibilityObject, setPossibilityObject] = useState({})

    const [form, setForm] = useState({
        plantImage: null,
        plantName: '',
        plantNickName: '',
        plantStartDate: '',
    });

    const handleSendImage = () => {
        const formData = new FormData();
        formData.append('plantImage', form.plantImage);

        axios.post(`${AI_IP}/predict`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
        .then((res) => {
            setPossibilityObject(res.data)
            setIsShowPossibility(true)
        })
    }

    const handlePlantNaming = (e) => {
        setForm({ ...form, plantName: e.currentTarget.querySelector('#plantNameContent').textContent });
        setIsShowPossibility(false);
    }

    const handleRegister = () => {
        requestRegister(form.plantImage, form.plantName, form.plantNickName)
        dispatch(fetchUser());
        window.location.reload();
    }

    const requestRegister = (plantImage, plantName, plantNickName) => {
        const formData = new FormData();
        formData.append('plantImage', plantImage);
        formData.append('plantName', plantName);
        formData.append('plantNickName', plantNickName);

        axios.post(`${Server_IP}/kit/${kitNo}/plant`, formData, {
            headers: {
                "Authorization": `Bearer ${accessToken}`
            },
        })
            .then(response => {
                alert('Your plant is registered!');
            })
            .catch(error => {
                alert("Please try again");
            });
    }


    



    return (
        <div>
            <button type="button" style={{ color: 'white', backgroundColor: '#7E8287', paddingLeft: '7px', paddingRight: '7px' }} className="btn" data-bs-toggle="modal" data-bs-target={`#registerPlant${kitNo}Modal`} data-bs-whatever="@getbootstrap">Register Plant</button>
            <div className="modal fade" id={`registerPlant${kitNo}Modal`} tabindex="-1" aria-labelledby="registerPlantLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content" style={{ height: isShowPossibility ? '600px' : '600px' }}>
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="exampleModalLabel">Register Plant</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <form>
                                <div className="mb-3">
                                    <div className='imgContent' style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginBottom: '20px' }}>
                                        <h5> Upload your plant Image</h5>
                                        <img style={{ border: '1px solid black', width: '100px', height: '100px'}} src={form.plantImage ? URL.createObjectURL(form.plantImage) : 'plantplant.png'} class="rounded-circle" alt="..."></img>
                                    <ToolTip></ToolTip>
                                    </div>
                                    <div class='sendImgContent' style={{ display: 'flex', justifyContent: 'right' }}>
                                        <button onClick={() => fileInputRef.current.click()} id='uploadBtn' style={{ borderRadius: '10px', borderRight: '1px solid white', borderTopRightRadius: '0px', borderBottomRightRadius: '0px', backgroundColor: '#73BD72', color: "white" }} type="button" className="btn">Select</button>
                                        <button onClick={handleSendImage} style={{ borderRadius: '10px', borderTopLeftRadius: '0px', borderLeft: '1px solid white', borderBottomLeftRadius: '0px', backgroundColor: '#73BD72', color: "white" }} type="button" className="btn">Check</button>
                                        <input ref={fileInputRef} onChange={(e) => setForm({ ...form, plantImage: e.target.files[0] })} style={{ display: 'none' }} type="file" id="input-file" />
                                    </div>
                                    {isShowPossibility ?
                                        <div style={{ marginTop: '10px', fontSize: '15px' }}>
                                            Choose your plant
                                            {Object.entries(possibilityObject).map((value, index) => {
                                                return (
                                                    <div key={index}>
                                                        <button className='possibilityBtn' style={{ backgroundColor: '#9CAFB7' , borderRadius: '10px', border: 'none', margin: '5px 5px 5px 0px' }} onClick={handlePlantNaming}>
                                                            <span id='plantNameContent'>{`${value[1].plantName}`}</span>
                                                            <span >&emsp;{`${(value[1].possibility * 100).toFixed(2)} %`}</span>
                                                        </button>
                                                    </div>
                                                )
                                            })}
                                        </div>
                                    : ''
                                    }
                                    {
                                        !isShowPossibility && Object.keys(possibilityObject).length > 0 ?
                                            <>
                                                <label htmlFor="recipient-name" className="col-form-label"><h5>What is your plant</h5></label>
                                                <input placeholder="Plant's name" type="text" className="form-control" id="recipient-name" value={form.plantName} onChange={(e) => setForm({ ...form, plantName: e.target.value })} readOnly />
                                            </>
                                            : null
                                        }
                                        <label htmlFor="recipient-name" className="col-form-label" > <h5>What is your plant's nickname</h5> </label>
                                        <input placeholder="Plant's Nickname" type="text" className="form-control" id="recipient-email" value={form.plantNickName} onChange={(e) => setForm({ ...form, plantNickName: e.target.value })} />
                                </div>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" style={{ color: 'white', background: '#797B84' }} className="btn" data-bs-dismiss="modal">Close</button>
                            <button type="button" style={{ color: 'white', backgroundColor: '#73BD72' }} disabled={!form.plantName || !form.plantNickName} className="btn" data-bs-dismiss="modal" onClick={handleRegister}>Register</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}




export default RegisterPlant;