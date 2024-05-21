import { createContext, useContext, useState } from "react";

const IsLoginContext = createContext();

export const IsLoginProvider = ({ children }) => {
    const [isLogin, setIsLogin] = useState('');

    return (
        <IsLoginContext.Provider value={{ isLogin, setIsLogin }}>
            {children}
        </IsLoginContext.Provider>
    )
}

export const useIsLogin = () => {
    return useContext(IsLoginContext);
}