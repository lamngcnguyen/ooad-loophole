class AttachmentTable {
    constructor(element) {
        this.html = $('.jquery-template .attachments').clone();
        const fileInput = this.html.find('input[name="specFiles"]');
        fileInput.change(function () {
            this.loadInput(fileInput.prop('files'));
        }.bind(this));
        this.html.find('.button.file-trigger').click(function () {
            fileInput.click();
        });
        element.append(this.html);
    }

    loadInput(files) {
        this.addAttachments(files, true);
    }

    loadJSON(files) {
        this.addAttachments(files, false);
    }

    clear() {
        this.html.children('tr:gt(1)').remove();
    }

    addAttachments(files, isNew) {
        for (var i = 0; i < files.length; i++) {
            const status = new Attachment(files[i], isNew);
            this.html.append(status.html);
        }
    }
}

class Attachment {
    constructor(file, isNew) {
        this.file = file;
        this.html = $('.jquery-template .spec-upload-status').clone();
        this.loader = this.html.find('.loader');
        this.uploadSuccess = this.html.find('.upload-success');
        this.uploadFailure = this.html.find('.upload-failure');
        this.btnDelete = this.html.find('div.delete-file');
        this.btnRetry = this.html.find('div.retry-upload');
        if (!isNew) {
            this.uploadSuccess.show();
            this.loader.hide();
            this.btnRetry.remove();
        } else {
            this.btnRetry.click(this.requestUpload);
            this.btnDelete.hide();
            this.btnRetry.show();
        }
    }

    requestUpload() {
        console.log('request upload called');
    }

    destroy() {
        this.html.remove();
    }
}