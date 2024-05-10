import React from 'react'
import { useState } from 'react'
import axios from 'axios'


function FindId(props) {

    const Server_IP = process.env.REACT_APP_Server_IP;

    const [form, setForm] = useState({
        userName: '',
        userEmail: '',
    })

    const handleFindId = () => {
        requestFindId(form.userName, form.userEmail)
    }

    const requestFindId = async (userName, userEmail) => {
        await axios.post(`${Server_IP}/auth/findId`, { userName: userName, userEmail: userEmail },
            {
                headers: {
                    "Content-Type": "application/json",
                },
            })
            .then((res) => {
                if (res?.data?.userId) {
                    alert(`Your ID is ${res.data.userId}`)
                } else {
                    alert('Not found')
                }
            })
            .catch((error) => {
                alert("Server Error");
            })
    }

    return (
        <div style={props.style}>
            <button style={{ border: 'none', backgroundColor: '#5B8C5A', padding: '7px 20px 7px 20px' }} type="button" className="btn btn-primary" data-bs-toggle="modal" data-bs-target="#findIdModal" data-bs-whatever="@getbootstrap">Find ID</button>
            <div className="modal fade" id="findIdModal" tabindex="-1" aria-labelledby="findIdModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content" style={{ height: '350px' }}>
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="exampleModalLabel">Find ID</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <form>
                                <div className="mb-3" style={{ fontSize: '15px' }}>
                                    <label htmlFor="recipient-name" className="col-form-label" >Your name</label>
                                    <input type="text" className="form-control" id="recipient-name" value={form.userName} onChange={(e) => setForm({ ...form, userName: e.target.value })} />
                                    <label htmlFor="recipient-name" className="col-form-label" >Your Email</label>
                                    <input type="email" className="form-control" id="recipient-email" value={form.userEmail} onChange={(e) => setForm({ ...form, userEmail: e.target.value })} />
                                </div>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={handleFindId}>Check</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}




export default FindId;