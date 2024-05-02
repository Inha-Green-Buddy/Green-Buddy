import React from 'react'
import { useEffect } from 'react';
import { useState } from 'react'
import axios from 'axios';
import Cookies from 'js-cookie';
import { useDispatch } from 'react-redux';
import { fetchUser } from '../../../store/userInfoSlice';

function KitDelete({kitNo}) {

  const dispatch = useDispatch();

  const Server_IP = process.env.REACT_APP_Server_IP;
  const accessToken = Cookies.get('accessToken');

  const [text, setText] = useState('');
  const [isDisabled, setIsDisabled] = useState(true);

  useEffect(() => {
    text == 'I want to delete this kit' ? setIsDisabled(false) : setIsDisabled(true)
  }, [text])

  const kitDelete = async () => {
    alert(kitNo);
    await axios.delete(`${Server_IP}/users/kit/${kitNo}`,
      {
        headers: {
          "Authorization": `Bearer ${accessToken}`
        },
      })
      .then((res) => {
        alert("Kit is Deleted")
        dispatch(fetchUser());
      })
      .catch((error) => {
        alert("Server error");
      })
  }

  return (
    <div>
      <button type="button" style={{ color: 'white', backgroundColor: '#F26419', paddingLeft: '7px', paddingRight: '7px' }} className="btn" data-bs-toggle="modal" data-bs-target={`#deleteKitModal${kitNo}`} data-bs-whatever="@getbootstrap">Kit delete</button>
      <div className="modal fade" id={`deleteKitModal${kitNo}`} tabindex="-1" aria-labelledby="deleteKitModalLabel" aria-hidden="true">
        <div className="modal-dialog">
          <div className="modal-content" style={{ height: '330px' }}>
            <div className="modal-header">
              <h1 className="modal-title fs-5">Kit Delete</h1>
              <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div className="modal-body">
              <form>
                <div style={{ textAlign: 'center' }} className="mb-3">
                  <label  htmlFor="recipient-name" className="col-form-label" ><h3>If you want to delete this device, <br/>follow the sentence below</h3></label>
                  <p><h4 style={{ fontWeight: 'bold' }}>I want to delete this kit</h4></p>
                  <input placeholder='I want to delete this kit' type="text" style={{ fontSize: '20px', fontWeight: 'bold', width: '100%' }}  onChange={(e) => setText(e.target.value)} />
                  <button type="button" style={{ marginTop: '20px', width: '100%', border: 'none', backgroundColor: isDisabled ? 'grey' : '#F26419' }} disabled={isDisabled} className="btn btn-primary" data-bs-dismiss="modal" onClick={kitDelete}>Delete this kit</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default KitDelete
