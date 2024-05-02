import React from 'react'
import axios from 'axios'
import Cookies from 'js-cookie';

function PlantFinish({ kitNo }) {

  const Server_IP = process.env.REACT_APP_Server_IP;

  const handlePlantFinish = async () => {
    await axios.post(`${Server_IP}/kit/${kitNo}/growth`,{}, 
    {
      headers: {
        "Authorization": `Bearer ${Cookies.get('accessToken') }`
      },
    })
    .then((res) => {
      alert('Plant is finished')
      window.location.reload();
    })
    .catch((err) => {
      console.log(err)
    })
  }
  
  return (
    <div>
      <button type="button" data-bs-dismiss="modal" onClick={handlePlantFinish} style={{ color: 'white', backgroundColor: '#73BD72' }} className="btn" >Growth Finish</button>
    </div>
  )
}

export default PlantFinish
