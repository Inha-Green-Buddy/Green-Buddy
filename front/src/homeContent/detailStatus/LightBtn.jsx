import React from 'react'
import axios from 'axios'
import Cookies from 'js-cookie';

function LightBtn({ kitNo }) {

    const Server_IP = process.env.REACT_APP_Server_IP;

    const requestLighting = async () => {
        const accessToken = Cookies.get('accessToken');
        await axios.get(`${Server_IP}/kit/${kitNo}/light`,
            {
                headers: {
                    "Authorization": `Bearer ${accessToken}`
                },
            })
            .then((res) => {
                alert('Lighting')
            })
            .catch((err) => {
                console.log(err)
            })
    }

    return (
        <div>
            <button type="button" style={{ color: 'white', backgroundColor: '#dfd880', fontSize: '12px', border: 'none', borderRadius: '10px', width: '7vmax', height: '25px', marginTop: '5px', padding: '0 auto' }} onClick={() => requestLighting()} >Light</button>
        </div>
    )
}

export default LightBtn
