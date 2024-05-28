import React from 'react'
import axios from 'axios';
import { useState, useEffect } from 'react';
import Cookies from 'js-cookie';

function ShowImages({ plantNum }) {

    const fetchImages = async () => {
        await axios.get(`${Server_IP}/kit/plant/${plantNum}/pictures`,
            {
                headers: {
                    "Authorization": `Bearer ${Cookies.get("accessToken")}`
                },
            })
            .then((res) => {
                setImages(res.data.reverse());
            })
            .catch((error) => {
                alert("Server error");
            })
    }

    const Server_IP = process.env.REACT_APP_Server_IP;
    const [images, setImages] = useState([]);

    useEffect(() => {
        fetchImages();
    }, []);

    return (
        <div>
            <div id={`carouselExampleDark${plantNum}`} className="carousel slide">
                <div className="carousel-indicators" style={{ top: '20px' }}  >
                    {images.map((image, index) => (
                        <button
                            key={index}
                            type="button"
                            data-bs-target={`#carouselExampleDark${plantNum}`}
                            data-bs-slide-to={index}
                            className={index === 0 ? "active" : ""}
                            aria-current="true"
                            aria-label={`Slide ${index + 1}`}
                        ></button>
                    ))}
                </div>

                <div className="carousel-inner" style={{ marginTop: '10px' }}>
                    {images.map((image, index) => (
                        <div
                            key={index}
                            className={`carousel-item ${index === 0 ? "active" : ""}`}
                            data-bs-interval="10000"
                        >
                            <img
                                src={`${image.imageUrl}`}
                                className="d-block w-100"
                            />
                            <div style={{ marginTop: '20px', position: 'relative' }}>
                                <div style={{ fontSize: '20px', wordWrap: 'break-word', maxWidth: '90%' }}>
                                    {image.msg}
                                </div>
                                <div style={{ marginTop: '10px' }}>
                                    <h5 style={{ marginRight: '0px', textAlign: 'right', fontSize: '12px', fontWeight: 'bold', color: 'grey' }}> <span style={{ fontSize: '15px' }}>Memory in </span>  {image.date.split(".")[0].replace("T", " ")}</h5>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>


                <button className="carousel-control-prev" type="button" data-bs-target={`#carouselExampleDark${plantNum}`} data-bs-slide="prev">
                    <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span className="visually-hidden">Previous</span>
                </button>
                <button className="carousel-control-next" type="button" data-bs-target={`#carouselExampleDark${plantNum}`} data-bs-slide="next">
                    <span className="carousel-control-next-icon" aria-hidden="true"></span>
                    <span className="visually-hidden">Next</span>
                </button>
            </div>
        </div>
    )

}

export default ShowImages
