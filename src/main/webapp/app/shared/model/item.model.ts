import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IItem {
  id?: string;
  status?: Status;
  tags?: string;
  text?: string;
  publishTime?: Moment;
  contentContentType?: string;
  content?: any;
  owner?: IUser;
}

export const defaultValue: Readonly<IItem> = {};
