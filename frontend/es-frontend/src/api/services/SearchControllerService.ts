/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_IPage_ } from "../models/BaseResponse_IPage_";
import type { CommonQueryRequest } from "../models/CommonQueryRequest";

import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class SearchControllerService {
  /**
   * listObjectByPage
   * @param commonQueryRequest commonQueryRequest
   * @returns BaseResponse_IPage_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static listObjectByPageUsingPost(
    commonQueryRequest: CommonQueryRequest
  ): CancelablePromise<BaseResponse_IPage_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/search/list",
      body: commonQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
