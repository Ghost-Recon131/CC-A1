import cookie from "js-cookie";
import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import {getGlobalState} from "../../utils/globalState";

export default function Component() {
    var navigate = useNavigate();
    var [user, setUser] = useState({});
    var queryParams = new URLSearchParams(window.location.search);


    useEffect(async () => {
        if (cookie.get("user")) {
            user = JSON.parse(cookie.get("user"));
            setUser(user);
        } else {
            navigate("/signin");
        }

        var paymentId = queryParams.get("token");
        console.log("Gotten paymentId: " + paymentId);

        // Post to backend to confirm payment made
        await axios.put(getGlobalState("backendDomain2") + "/api/Transactions/successPayment?token=" + paymentId);

        navigate("/viewtransactions");
    }, []);


    return (
        <div>
            <h2 className="text-3xl font-bold text-white shadow-md rounded pt-2 pb-8 mb-4">Purchase successful</h2>
            <h4 className="text-3xl font-bold text-white shadow-md rounded pt-2 pb-8 mb-4">Redirecting to transaction history...</h4>
        </div>
    );
}
