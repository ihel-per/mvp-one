import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { openFile, byteSize, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './item.reducer';
import { IItem } from 'app/shared/model/item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IItemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IItemState = IPaginationBaseState;

export class Item extends React.Component<IItemProps, IItemState> {
  state: IItemState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { itemList, match } = this.props;
    return (
      <div>
        <h2 id="item-heading">
          Items
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Item
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            {itemList && itemList.length > 0 ? (
              <Table responsive aria-describedby="item-heading">
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      ID <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('status')}>
                      Status <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('tags')}>
                      Tags <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('text')}>
                      Text <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('publishTime')}>
                      Publish Time <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('content')}>
                      Content <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      Owner <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {itemList.map((item, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${item.id}`} color="link" size="sm">
                          {item.id}
                        </Button>
                      </td>
                      <td>{item.status}</td>
                      <td>{item.tags}</td>
                      <td>{item.text}</td>
                      <td>
                        <TextFormat type="date" value={item.publishTime} format={APP_DATE_FORMAT} />
                      </td>
                      <td>
                        {item.content ? (
                          <div>
                            <a onClick={openFile(item.contentContentType, item.content)}>Open &nbsp;</a>
                            <span>
                              {item.contentContentType}, {byteSize(item.content)}
                            </span>
                          </div>
                        ) : null}
                      </td>
                      <td>{item.owner ? item.owner.id : ''}</td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${item.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${item.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${item.id}/delete`} color="danger" size="sm">
                            <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="alert alert-warning">No Items found</div>
            )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ item }: IRootState) => ({
  itemList: item.entities,
  totalItems: item.totalItems,
  links: item.links,
  entity: item.entity,
  updateSuccess: item.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Item);
