import { getGlobalState, setGlobalState } from "utils/globalState";


export default function Component() {
    return (
      <div>
       {getGlobalState('jwt')}
      </div>
    );
  }
  
  
  