const INIT_CODE = 'code/INIT_CODE'
const INIT_AUTH = 'code/INIT_AUTH'

export const initCode = (code) => ({ type: INIT_CODE, code })
export const initAuth = (code) => ({ type: INIT_AUTH, code })

const initialState = {
  commonCode: undefined,
  authCode: undefined,
}

export default function rule(state = initialState, action) {
  switch (action.type) {
    case INIT_CODE:
      console.log('init code')
      return {
        ...state,
        commonCode: action.code
      }
    case INIT_AUTH:
      console.log('init auth')
      return {
        ...state,
        authCode: action.code
      }
    default:
      return state;
  }
}