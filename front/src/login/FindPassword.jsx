import React from 'react'
import { useState } from 'react'
import axios from 'axios'


function FindPassword(props) {

    const Server_IP = process.env.REACT_APP_Server_IP;

    const [form, setForm] = useState({
        userId: '',
        userEmail: '',
    })

    const handleFindPassword = () => {
        requestFindPassword(form.userId, form.userEmail)
    }

    const requestFindPassword = async (userId, userEmail) => {
        await axios.post(`${Server_IP}/auth/findPw`, { userId: userId, userEmail: userEmail },
            {
                headers: {
                    "Content-Type": "application/json",
                },
            })
            .then((res) => {
                alert(res.data);
                
            })
            .catch((error) => {
                alert("Server error");
            })
    }

    return (
        <div style={{ marginRight: '10px' }}>
            <button style={{ border: 'none', backgroundColor: '#5B8C5A', padding: '7px 15px 7px 15px' }} type="button" className="btn btn-primary" data-bs-toggle="modal" data-bs-target="#findPasswordModal" data-bs-whatever="@getbootstrap">Find Password</button>
            <div className="modal fade" id="findPasswordModal" tabindex="-1" aria-labelledby="findIdModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content" style={{ height: '350px' }}>
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="exampleModalLabel">Find Pw</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body" style={{ fontSize: '15px' }} >
                            <form>
                                <div className="mb-3">
                                    <label htmlFor="recipient-name" className="col-form-label" >ID</label>
                                    <input type="text" className="form-control" id="recipient-name" value={form.userId} onChange={(e) => setForm({ ...form, userId: e.target.value })} />
                                    <label htmlFor="recipient-name" className="col-form-label" >E-mail</label>
                                    <input type="email" className="form-control" id="recipient-email" value={form.userEmail} onChange={(e) => setForm({ ...form, userEmail: e.target.value })} />
                                </div>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" className="btn btn-primary" data-bs-dismiss="modal" onClick={handleFindPassword}>Check</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}




export default FindPassword