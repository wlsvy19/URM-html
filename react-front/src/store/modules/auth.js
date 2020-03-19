const LOGIN = 'auth/LOGIN'
const LOGOUT = 'auth/LOGOUT'

export const login = (user) => ({ type: LOGIN, user })
export const logout = () => ({ type: LOGOUT });

const initialState = {
  logged: false,
  userInfo: {
    id: '',
    name: '',
    auth: undefined,
  },
}

export default function rule(state = initialState, action) {
  switch (action.type) {
    case LOGIN:
      console.log('login')
      return {
        ...state,
        logged: true,
        userInfo: {
          id: action.user.id,
          name: action.user.name,
          auth: action.user.authId,
        }
      }
    case LOGOUT:
      console.log('logout')
      return {
        ...state,
        logged: false,
        userInfo: initialState.userInfo
      }
    default:
      return state;
  }
}