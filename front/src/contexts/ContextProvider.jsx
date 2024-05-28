import { AccessTokenProvider } from "./AccessTokenContext"
import { IsLoginProvider } from "./IsLoginContext";

const ContextProvider = ({ children }) => {
    return (
        <IsLoginProvider>
            <AccessTokenProvider>
                {children}
            </AccessTokenProvider>
        </IsLoginProvider>
    )
}

export default ContextProvider;