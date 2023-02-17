export class LoginForm {
  user: string;
  password: string;

  constructor(user: string, password: string) {
    this.user = user;
    this.password = password;
  }
}

export class RegisterForm {
  name: string;
  username: string;
  email: string;
  password: string;
  dateOfBirth: string;
  nickname: string;

  constructor(name: string, username: string, email: string, password: string, dateOfBirth: string, nickname: string) {
    this.name = name;
    this.username = username;
    this.email = email;
    this.password = password;
    this.dateOfBirth = dateOfBirth;
    this.nickname = nickname;
  }
}

export class JwtResponse {
  accessToken: string;
  type: string;
  username: string;
  name: string;
  email: string;
  authorities: string[];
  userId: number;
  refreshToken: string;
}
