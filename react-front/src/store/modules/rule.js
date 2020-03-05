const INIT_CODE = 'rule/INIT_CODE'

export const initCode = (code) => ({ type: INIT_CODE, code })

const initialState = {
  commonCode: undefined,
}

export default function rule(state = initialState, action) {
  switch (action.type) {
    case INIT_CODE:
      console.log(action.code)
      return {
        ...state,
        commonCode: action.code
      }
    default:
      return state;
  }
}