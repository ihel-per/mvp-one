import { IUser } from 'app/shared/model/user.model';

export interface IProfile {
  id?: string;
  nick?: string;
  sencha?: string;
  owner?: IUser;
}

export const defaultValue: Readonly<IProfile> = {};
