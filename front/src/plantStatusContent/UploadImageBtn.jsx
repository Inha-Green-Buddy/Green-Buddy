import React from 'react'
import Cookies from 'js-cookie';
import { useRef, useState } from 'react';
import axios from 'axios';

function UploadImageBtn({ plantNum }) {

    const Server_IP = process.env.REACT_APP_Server_IP;

    const fileInputRef = useRef();

    const [form, setForm] = useState({
        plantImage: null,
        plantMsg: '',
    });

    const handleUploadImage = () => {
        const formData = new FormData();
        formData.append('file', form.plantImage);
        formData.append('msg', form.plantMsg)

        axios.post(`${Server_IP}/kit/plant/${plantNum}/picture`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                "Authorization": `Bearer ${Cookies.get("accessToken")}`
            }
            })
            .then((res) => {
                alert("image is uploaded")
                window.location.reload();
            })
            .catch((err) => {
                console.log(err);
            })
    };

    return (
        <div>
            <button style={{ color: 'white', backgroundColor: '#73BD72', paddingLeft: '7px', paddingRight: '7px' }} type="button" class="btn" data-bs-toggle="modal" data-bs-target={`#uploadImgModal${plantNum}`}>
                Upload Image
            </button>
            <div class="modal fade" id={`uploadImgModal${plantNum}`} tabindex="-1" aria-labelledby="carouselModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-md">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="carouselModalLabel">Upload Image</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div style={{ display: 'flex', justifyContent: 'center'}}>
                                <img style={{ margin: '0 auto', border: '1px solid black', width: '100px', height: '100px' }} src={form.plantImage ? URL.createObjectURL(form.plantImage) : 'plantplant.png'} class="rounded-circle" alt="..."></img>
                            </div>
                            <div class='sendImgContent' style={{ marginTop: '10px', marginBottom: '10px', display: 'flex', justifyContent: 'center' }}>
                                <button onClick={() => fileInputRef.current.click()} id='uploadBtn' style={{ borderRadius: '10px', borderRight: '1px solid white', backgroundColor: '#73BD72', color: "white" }} type="button" className="btn">Select</button>
                                <input ref={fileInputRef} onChange={(e) => setForm({ ...form, plantImage: e.target.files[0] })} style={{ display: 'none' }} type="file" id="input-file" />
                            </div>
                            <input placeholder="Enter your massage" type="text" className="form-control" id="recipient-email" value={form.plantMsg} onChange={(e) => setForm({ ...form, plantMsg: e.target.value })} />
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button data-bs-dismiss="modal" aria-label="Close" onClick={handleUploadImage} style={{ borderRadius: '10px', backgroundColor: '#73BD72', color: "white" }} type="button" className="btn">Upload</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default UploadImageBtn
