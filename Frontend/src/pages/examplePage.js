import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class ExamplePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onGetPriorityItems', 'onGetAllItems', 'renderItems'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    mount() {
        document.getElementById('all-priority-items').addEventListener('click', this.onGetPriorityItems);
        document.getElementById('all-priority-items').addEventListener('click', this.renderItems);
        document.getElementById('all-items').addEventListener('click', this.onGetAllItems);
        this.client = new ExampleClient();
        this.dataStore.addChangeListener(this.renderItems());
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderItems() {

        let resultArea = document.getElementById("allItemsToAdd");

        const items = this.dataStore.get("priorityItems");

        let fullHTML = "";

        if(items){
            fullHTML += items;
            // for (let item of items) {
            //     // use the comment in the HTML
            //     fullHTML += `
            //         <div class="card" id="${item.itemId}">
            //             <td>${item.itemId}</td>
            //             <td>${item.description}</td>
            //             <td>${item.currentQty}</td>
            //             <td>${item.reorderQty}</td>
            //             <td>${item.qtyTrigger}</td>
            //             <td>${item.orderDate}</td>
            //             <td><input class="btn" id="update-arrow" type="button" value="Update" onclick="openUpdateMessageForm(this)"></td>
            //         </div>
            //     `;
            // }
            resultArea.innerHTML += fullHTML;
        } else {
            resultArea.innerHTML = "No Item";
        }
    }

    // async onGetSample(event) {
    //     // Prevent the page from refreshing on form submit
    //     event.preventDefault();
    //
    //     let id = document.getElementById("create-sample-song-form").value;
    //     this.dataStore.set("songSamples", null);
    //
    // }

    //Event handlers -------------------------------------------------------------------------------------------------

    async onGetPriorityItems() {
        let items = this.client.getPriorityList(this.errorHandler);

        this.dataStore.set("priorityItems", items);

        if (items) {
            this.showMessage(`Got priority items list!` + items);
        } else {
            this.errorHandler("Error generating priority list!  Try again...");
        }
    }

    async onGetAllItems() {

        let itemsHtml = "";

        const items = this.dataStore.get(this.errorHandler)

        if(items) {
            for (const item of items) {
                itemsHtml += `                                              
                    <tr class="card" id="${item.itemId}">
                        <td>${item.itemId}</td>
                        <td>${item.description}</td>
                        <td>${item.currentQty}</td>                     
                        <td>${item.reorderQty}</td>
                        <td>${item.qtyTrigger}</td>
                        <td>${item.orderDate}</td>                                      
                        <td><input class="btn" id="update-arrow" type="button" value="Update" onclick="openUpdateMessageForm(this)"></td>
                    </tr>                            
                `;
            }
        }
        document.getElementById("allItemsToAdd").innerHTML += itemsHtml;
    }

    // Event Handlers --------------------------------------------------------------------------------------------------

}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const examplePage = new ExamplePage();
    examplePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
