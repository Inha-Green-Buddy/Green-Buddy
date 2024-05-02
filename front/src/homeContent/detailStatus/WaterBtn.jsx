import React from 'react'
import axios from 'axios'
import Cookies from 'js-cookie';

function WaterBtn({ kitNo, fetchWateringDates }) {

    const Server_IP = process.env.REACT_APP_Server_IP;

    const requestWater = async () => {
        const accessToken = Cookies.get('accessToken');
        await axios.get(`${Server_IP}/kit/${kitNo}/water`,
            {
                headers: {
                    "Authorization": `Bearer ${accessToken}`
                },
            })
            .then((res) => {
                fetchWateringDates();
                alert('Watered')
            })
            .catch((err) => {
                console.log(err)
            })
    }

    return (
        <div>
            <button
                type="button"
                style={{
                    color: 'white',
                    backgroundColor: '#80cee1',
                    fontSize: '12px',
                    border: 'none',
                    borderRadius: '10px',
                    // width: '50px',
                    width: '7vmax',
                    height: '25px',
                    marginBottom: '5px',
                    display: 'flex',  
                    alignItems: 'center',
                    justifyContent: 'center'  
                }}
                onClick={() => requestWater()}
            >
                Water
            </button>
        </div>

    )
}

export default WaterBtn
