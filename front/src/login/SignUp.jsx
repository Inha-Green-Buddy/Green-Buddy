import { useState, useEffect } from 'react'
import { useFetch } from '../hooks/useFetch';

function SignUp() {

    const { statusCode: signUpStatusCode, postReq: signUpPost } = useFetch();
    const { getReq, postReq } = useFetch();
    const { statusCode: validateIdStatusCode, postReq: validateIdPost } = useFetch();
    const { statusCode: validateEmailStatusCode, postReq: validateEmailPost } = useFetch();
    const { statusCode: sendAuthCodeStatusCode, postReq: sendAuthCodePost } = useFetch();
    const { statusCode: validateAuthCodeStatusCode, postReq: validateAuthCodePost } = useFetch();
    const [isAuthCode, setIsAuthCode] = useState(false);
    const [isEmailChecked, setIsEmailChecked] = useState(false);
    const [isIdChecked, setIsIdChecked] = useState(false);

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
            if (key === 'authCode') {
                continue
            }
            if (!form[key]) {
                return false;
            }
        }
        return !validationMsg.userEmail && !validationMsg.userPhoneNum;
    }
    
    const [form, setForm] = useState({
        userName: '',
        userId: '',
        userPassword: '',
        userEmail: '',
        userPhoneNum: '',
        userNickname: '',
        authCode: '',
    })
    
    const createAccount = async (userName, userId, userPassword, userEmail, userPhoneNum, userNickname) => {
        await signUpPost({
            url: `auth/join`,
            data:{ userName: userName, userId: userId, userPassword: userPassword, userEmail: userEmail, userPhoneNum: userPhoneNum, userNickname: userNickname },
            token: false,
        })
    }
    
    useEffect(() => {
        if (signUpStatusCode) {
            if (signUpStatusCode === 200) {
                alert('Complete')
            } else {
                alert('please try again')
            }
        }
    }, [signUpStatusCode])
    
    const checkId = async (e) => {
        e.preventDefault();
        await validateIdPost({
            url: 'auth/validate/id',
            data: { userId: form.userId },
            token: false,
        })
    }
    
    // checkId
    useEffect(() => {
        if (validateIdStatusCode) {
            if (validateIdStatusCode === 200) {
                setIsIdChecked(true);
            } else {
                alert('already exists')
                setForm({ ...form, userId: '' })
            }
        }
    }, [validateIdStatusCode])
    
    const checkEmail = async (e) => {
        e.preventDefault();
        await validateEmailPost({
            url: 'auth/validate/email',
            data: { userEmail: form.userEmail },
            token: false,
        })
    }

    // checkEmail
    useEffect(() => {
        if (validateEmailStatusCode) {
            if (validateEmailStatusCode === 200) {
                sendAuthCodePost({
                    url: 'auth/verify/email',
                    data: { userEmail: form.userEmail },
                    token: false,
                })
                setIsAuthCode(true);
                alert('Please check your email and enter the verification code')
            } else {
                alert('already exists')
                setForm({ ...form, userEmail: '' })
            }
        }
    }, [validateEmailStatusCode])
                
    const checkAuthCode = async (e) => {
        e.preventDefault();
        await validateAuthCodePost({
            url: 'auth/verify/code',
            data: { userEmail: form.userEmail, code: form.authCode },
            token: false,
        })
    }

    // authcode
    useEffect(() => {
        if (validateAuthCodeStatusCode) {
            if (validateAuthCodeStatusCode === 200) {
                setIsAuthCode(false);
                setIsEmailChecked(true);
            } else {
                alert('please check auth code')
            }
        }
    }, [validateAuthCodeStatusCode])

    return (
        <div>
            <button style={{ border: 'none', backgroundColor: '#5B8C5A', padding: '7px 20px 7px 20px' }} type="button" className="btn btn-primary" data-bs-toggle="modal" data-bs-target="#signUpModal" data-bs-whatever="@getbootstrap">Sign Up</button>
            <div className="modal fade" id="signUpModal" tabindex="-1" aria-labelledby="signUpModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content" style={{ height: '680px' }}>
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="exampleModalLabel">Sign Up</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <form>
                                <div className="mb-3" style={{ fontSize: '15px' }} >
                                    <div className='flex flex-row justify-between w-full align-baseline mb-2'>
                                        <div className='align-middle'>
                                            ID
                                        </div>
                                        {isIdChecked ?
                                            (
                                                <div className='bg-sky-300 rounded-xl pl-2 pr-2 ml-2'>
                                                    checked
                                                </div>
                                            ) :
                                            (
                                                <button onClick={(e) => checkId(e)}>
                                                    <div className='bg-green-300 rounded-xl pl-2 pr-2 ml-2'>
                                                        check
                                                    </div>
                                                </button>
                                            )
                                        }
                                    </div>
                                    <input type="text" className="form-control" id="recipient-id" value={form.userId} onChange={(e) => setForm({...form, userId: e.target.value})} />
                                    <div className='flex flex-row justify-between w-full align-middle mb-2 mt-2'>
                                        <div>
                                            E-mail
                                        </div>
                                        {isAuthCode ?
                                            (
                                                <>
                                                    <div className='w-30 h-5 flex flex-row ml-24'>
                                                        <div className='text-sm mr-2'>
                                                            Code
                                                        </div>
                                                        <input type="text" className="form-control h-full" id="recipient-authCode" value={form.authCode} onChange={(e) => setForm({ ...form, authCode: e.target.value })} />
                                                    </div>
                                                    <button onClick={(e) => checkAuthCode(e)}>
                                                        <div className='bg-green-300 rounded-xl pl-2 pr-2 ml-2'>
                                                            check
                                                        </div>
                                                    </button>
                                                </>
                                            ) :
                                            (
                                                isEmailChecked ? (
                                                    <div className='bg-sky-300 rounded-xl pl-2 pr-2 ml-2'>
                                                        checked
                                                    </div>
                                                ) : (
                                                    <button onClick={(e) => checkEmail(e)}>
                                                        <div className='bg-green-300 rounded-xl pl-2 pr-2 ml-2'>
                                                            check
                                                        </div>
                                                    </button>
                                                )
                                            )
                                        }
                                    </div>
                                    <input type="email" className="form-control" id="recipient-email" value={form.userEmail} onChange={(e) => handleInputChange('userEmail', e.target.value)} />
                                    <label htmlFor="recipient-name" className="col-form-label" >Password</label>
                                    <input type="password" className="form-control" id="recipient-pw" value={form.userPassword} onChange={(e) => setForm({ ...form, userPassword: e.target.value })} />
                                    <label htmlFor="recipient-name" className="col-form-label" >Name</label>
                                    <input type="text" className="form-control" id="recipient-name" value={form.userName} onChange={(e) => setForm({ ...form, userName: e.target.value })} />
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