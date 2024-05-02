import React from 'react'
import { useEffect, useState, useRef } from 'react';
import axios from 'axios';

function FindPlant() {

    const plants = ['Angel Wing Begonia', 'Areca Palm', 'Broadleaf Lady Palm',
        'Fiddle Leaf Fig', 'Gardenia Jasminoides', 'Geranium',
        'Goldcrest Wilma', 'Golden Pothos', 'Heartleaf Philodendron',
        'Lucky Bamboo', 'Money Tree', 'Monstera Deliciosa', 'Rubber Plant',
        'Snake Plant', 'Cycad'];

    const [plantName, setPlantName] = useState('');
    const [searchName, setSearchName] = useState('');
    const [plantDetail, setPlantDetail] = useState(null);
    const [plantImage, setPlantImage] = useState(null);

    const AI_IP = process.env.REACT_APP_AI_IP;

    const fileInputRef = useRef();

    useEffect(() => {
        if (searchName) {
            receivePlantDetail();     
        }
    }, [searchName]);

    const receivePlantDetail = async () => {
        try {
            const res = await axios.get('/plantInfoData.json');
            setPlantDetail(res.data[searchName]);
        } catch (err) {
            console.error("Error fetching plant data:", err);
        }
    }

    const handleSendImage = () => {
        const formData = new FormData();
        formData.append('plantImage', plantImage);

        axios.post(`${AI_IP}/predict`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
            })
            .then((res) => {
                setPlantName(res.data[1].plantName)
            })
    }

    return (
        <div>
            <div>
                <div style={{ minHeight: '200px', alignItems: 'center', width: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                    <h5 style={{ marginTop: '30px', textAlign: 'center', padding: '5px' }}>For information on plants, <br/>
                        Upload a picture or enter the name of the plant</h5>   
                    <img style={{ marginTop: '30px', border: '1px solid black', width: '100px', height: '100px' }} src={plantImage ? URL.createObjectURL(plantImage) : 'plantplant.png'} class="rounded-circle" alt="..."></img>
                    <div style={{ marginTop: '10px' }}>
                        <button onClick={() => fileInputRef.current.click()} id='uploadBtn' style={{ borderRadius: '10px', borderRight: '1px solid white', borderTopRightRadius: '0px', borderBottomRightRadius: '0px', backgroundColor: '#73BD72', color: "white" }} type="button" className="btn">Select</button>
                        <button onClick={handleSendImage} style={{ borderRadius: '10px', borderTopLeftRadius: '0px', borderLeft: '1px solid white', borderBottomLeftRadius: '0px', backgroundColor: '#73BD72', color: "white" }} type="button" className="btn">Upload</button>
                    </div>
                </div>
                <input ref={fileInputRef} onChange={(e) => setPlantImage(e.target.files[0])} style={{ display: 'none' }} type="file" id="input-file" />
            </div>

            <div style={{ display: 'flex', justifyContent: 'center' }}>
                <div style={{ marginTop: '30px', marginBottom: '30px', display: 'flex', width: '70vmin'  }}>
                    <input style={{ borderRadius: '15px', borderTopRightRadius: '0px', borderBottomRightRadius: '0px', fontWeight: 'bold' }} autocomplete="off"  list='plants' placeholder="Plant's name" type="text" className="form-control" id="recipient-name" value={plantName} onChange={(e) => setPlantName(e.target.value)} />
                    <datalist id="plants">
                        {plants.map((plant, index) => (
                            <option key={index} value={plant} />
                        ))}
                    </datalist>
                    <button style={{ color: 'white', backgroundColor: '#73BD72', border: 'none', borderRadius: '15px', borderTopLeftRadius: '0px', borderBottomLeftRadius: '0px' }} type='button' onClick={() => setSearchName(plantName)} >Search</button>
                </div>
            </div>
            {plantDetail ?
                <div style={{ minHeight: '400px' }}>
                    <div style={{ padding: '10px', lineHeight: '35px' }}>
                        English Name, Korean Name : {plantDetail.plantEnName} / {plantDetail.plantKoName}<br />
                        Species : {plantDetail.plantKind} <br />
                        Least Illumninance: {plantDetail.plantLeastIllumninance} Lux<br />
                        Prefered Place : {plantDetail.plantPlace}<br />
                        Rasing Level : {plantDetail.plantLevel}<br />
                        Growth Rate : {plantDetail.plantGrowthRate}<br />
                        Prefered Tempearture : {plantDetail.plantTemp} ℃<br />
                        Least Temperature : {plantDetail.plantWinterTemp} ℃<br />
                        Humidity : {plantDetail.plantHumidity} %<br />
                        Fertility : {plantDetail.plantFertility}<br />
                        Spring Water : {plantDetail.plantSpringWater}<br />
                        Summer Water : {plantDetail.plantSummerWater}<br />
                        Autumn Water : {plantDetail.PlantAutumnWater}<br />
                        Winter Water : {plantDetail.plantWinterWater}<br />
                        Plant Info:
                        {plantDetail && plantDetail.plantInfo && typeof plantDetail.plantInfo === 'string' ?
                            plantDetail.plantInfo.split('\n').map((info, index) => (
                                <div key={index}>{index + 1 } - {info}</div>
                            ))
                            : null}
                        Plant GrowthType : {plantDetail.plantGrowthType}<br />
                        Plant Height : {plantDetail.plantHeight} cm<br />
                        Plant Area : {plantDetail.plantArea} cm<br />
                    </div>
            </div>
                : <div style={{ minHeight: '60px', textAlign: 'center', fontSize: '20px' }}> Please choose a plant above </div>
            }
        </div>
    )
}

export default FindPlant