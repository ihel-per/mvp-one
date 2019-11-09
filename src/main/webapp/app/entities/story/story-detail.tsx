import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './story.reducer';
import { IStory } from 'app/shared/model/story.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StoryDetail extends React.Component<IStoryDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { storyEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Story [<b>{storyEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{storyEntity.status}</dd>
            <dt>
              <span id="text">Text</span>
            </dt>
            <dd>{storyEntity.text}</dd>
            <dt>
              <span id="publishTime">Publish Time</span>
            </dt>
            <dd>
              <TextFormat value={storyEntity.publishTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="content">Content</span>
            </dt>
            <dd>
              {storyEntity.content ? (
                <div>
                  <a onClick={openFile(storyEntity.contentContentType, storyEntity.content)}>Open&nbsp;</a>
                  <span>
                    {storyEntity.contentContentType}, {byteSize(storyEntity.content)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>Owner</dt>
            <dd>{storyEntity.owner ? storyEntity.owner.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/story" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/story/${storyEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ story }: IRootState) => ({
  storyEntity: story.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StoryDetail);
