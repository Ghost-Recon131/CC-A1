import { createGlobalState } from 'react-hooks-global-state'

export let{ getGlobalState, setGlobalState } = createGlobalState({
  // TODO: Backend domain link change for AWS link once deployed
backendDomain: 'http://localhost:8080',
// stored as a cookie, created when signed in
jwt: null  
})
