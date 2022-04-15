import { getGlobalState, setGlobalState } from "utils/globalState";
import cookie from "js-cookie";
import { useEffect } from "react";
import {
  useNavigate,
} from 'react-router-dom';

export default function Component() {
  const navigate = useNavigate();

  useEffect(() => {
    // check if logged in
    // if (!cookie.get("jwt")) {
    //   navigate('/signin')
    // }
  }, []);

  return <div>HELLO THERE</div>;
}
