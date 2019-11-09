import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Story from './story';
import StoryDetail from './story-detail';
import StoryUpdate from './story-update';
import StoryDeleteDialog from './story-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={Story} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StoryDeleteDialog} />
  </>
);

export default Routes;
