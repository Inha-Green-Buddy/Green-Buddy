import React from 'react'
import { useState } from 'react'
import axios from 'axios'
import { useFetch } from '../hooks/useFetch';

function SignUp() {

    const [validationMsg, setValidationMsg] = useState({
        userEmail: '',
        userPhoneNum: '',
    });

    const handleValidation = (currentForm) => {
        let emailMsg = !currentForm.userEmail.includes('@') ? 'email must include @' : '';
        let phoneNumMsg = !/^\d{10,11}$/.test(currentForm.userPhoneNum) ? 'The phone number must be 10 or 11 numbers' : '';

        setValidationMsg({
            userEmail: emailMsg,
            userPhoneNum: phoneNumMsg,
        });

        return !emailMsg && !phoneNumMsg;
    };

    const handleInputChange = (field, value) => {
        const updatedForm = { ...form, [field]: value };
        setForm(updatedForm);
        handleValidation(updatedForm);
    };

    const handleSignUp = () => {
        createAccount(form.userName, form.userId, form.userPassword, form.userEmail, form.userPhoneNum, form.userNickname);
    };

    const isFormComplete = () => {
        for (let key in form) {
            if (!form[key]) {
                return false;
            }
        }
        return !validationMsg.userEmail && !validationMsg.userPhoneNum;
    }

    const Server_IP = process.env.REACT_APP_Server_IP;

    const [form, setForm] = useState({
        userName: '',
        userId: '',
        userPassword: '',
        userEmail: '',
        userPhoneNum: '',
        userNickname: '',
    })

    const {result, postReq} = useFetch();

    const createAccount = async(userName, userId, userPassword, userEmail, userPhoneNum, userNickname) => {
        postReq({
            url: `auth/join`,
            data:{ userName: userName, userId: userId, userPassword: userPassword, userEmail: userEmail, userPhoneNum: userPhoneNum, userNickname: userNickname },
            token: true,
        })
    } 

    return (
        <div>
            <button style={{ border: 'none', backgroundColor: '#5B8C5A', padding: '7px 20px 7px 20px' }} type="button" className="btn btn-primary" data-bs-toggle="modal" data-bs-target="#signUpModal" data-bs-whatever="@getbootstrap">Sign Up</button>
            <div className="modal fade" id="signUpModal" tabindex="-1" aria-labelledby="signUpModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content" style={{ height: '650px' }}>
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="exampleModalLabel">Sign Up</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <form>
                                <div className="mb-3" style={{ fontSize: '15px' }} >
                                    <label htmlFor="recipient-name" className="col-form-label" >ID</label>
                                    <input type="text" className="form-control" id="recipient-id" value={form.userId} onChange={(e) => setForm({...form, userId: e.target.value})} />
                                    <label htmlFor="recipient-name" className="col-form-label" >Password</label>
                                    <input type="password" className="form-control" id="recipient-pw" value={form.userPassword} onChange={(e) => setForm({ ...form, userPassword: e.target.value })} />
                                    <label htmlFor="recipient-name" className="col-form-label" >Name</label>
                                    <input type="text" className="form-control" id="recipient-name" value={form.userName} onChange={(e) => setForm({ ...form, userName: e.target.value })} />
                                    <label htmlFor="recipient-email" className="col-form-label">E-mail</label>
                                    <input type="email" className="form-control" id="recipient-email" value={form.userEmail} onChange={(e) => handleInputChange('userEmail', e.target.value)} />
                                    <div style={{ color: 'red' }}>{validationMsg.userEmail}</div>
                                    <label htmlFor="recipient-phonenum" className="col-form-label">Phone number</label>
                                    <input type="tel" className="form-control" id="recipient-phonenum" value={form.userPhoneNum} onChange={(e) => handleInputChange('userPhoneNum', e.target.value)} />
                                    <div style={{ color: 'red' }}>{validationMsg.userPhoneNum}</div><label htmlFor="recipient-name" className="col-form-label" >Nickname</label>
                                    <input type="text" className="form-control" id="recipient-nickname" value={form.userNickname} onChange={(e) => setForm({ ...form, userNickname: e.target.value })} />
                                </div>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" className="btn btn-primary" disabled={!isFormComplete()} data-bs-dismiss="modal" onClick={handleSignUp}>Submit</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default SignUp