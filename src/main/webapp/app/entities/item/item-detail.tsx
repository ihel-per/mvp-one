import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './item.reducer';
import { IItem } from 'app/shared/model/item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ItemDetail extends React.Component<IItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { itemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Item [<b>{itemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{itemEntity.status}</dd>
            <dt>
              <span id="tags">Tags</span>
            </dt>
            <dd>{itemEntity.tags}</dd>
            <dt>
              <span id="text">Text</span>
            </dt>
            <dd>{itemEntity.text}</dd>
            <dt>
              <span id="publishTime">Publish Time</span>
            </dt>
            <dd>
              <TextFormat value={itemEntity.publishTime} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="content">Content</span>
            </dt>
            <dd>
              {itemEntity.content ? (
                <div>
                  <a onClick={openFile(itemEntity.contentContentType, itemEntity.content)}>Open&nbsp;</a>
                  <span>
                    {itemEntity.contentContentType}, {byteSize(itemEntity.content)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>Owner</dt>
            <dd>{itemEntity.owner ? itemEntity.owner.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/item/${itemEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ item }: IRootState) => ({
  itemEntity: item.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ItemDetail);
