import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 */

// Ky is here to work on this :D
export default class ItemClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getPriorityList','getAllInventoryItems', 'updateItem', 'deleteByItemId', 'getItemById'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

   async getPriorityList(errorCallback) {
        try {
            const response = await this.client.get('/item/reorderNeed');
            return response.data;
        } catch (error) {
            this.handleError("getPriorityList", error, errorCallback);
        }
   }

   async getAllInventoryItems(errorCallback) {
       try {
           const response = await this.client.get('/item/all');
           return response.data;
       } catch (error) {
           this.handleError("getAllInventoryItems", error, errorCallback);
       }
   }

    async updateItem(itemId, description, currentQty, reorderQty, qtyTrigger, orderDate, errorCallback) {
        try {
            const response = await this.client.put('item', {
                itemId: itemId,
                description: description,
                currentQty: currentQty,
                reorderQty: reorderQty,
                qtyTrigger: qtyTrigger,
                orderDate: orderDate
            });
            return response.data;
        } catch (error) {
            this.handleError("updateItem", error, errorCallback);
        }
    }

    async deleteByItemId(itemId, errorCallback) {
        try {
            const response = await this.client.delete('/item/delete/${itemId}');
            return response.data;
        } catch (error) {
            this.handleError("deleteItemById", error, errorCallback);
        }
    }

    async getItemById(itemId, errorCallback) {
        try {
            const response = await this.client.get('/item/${itemId}');
            return response.data;
        } catch (error) {
            this.handleError("getItemById", error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
