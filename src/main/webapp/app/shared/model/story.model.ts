import { Moment } from 'moment';
import { IProfile } from 'app/shared/model/profile.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IStory {
  id?: string;
  status?: Status;
  text?: string;
  publishTime?: Moment;
  contentContentType?: string;
  content?: any;
  owner?: IProfile;
}

export const defaultValue: Readonly<IStory> = {};
