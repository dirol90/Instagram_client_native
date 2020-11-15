/**
 * Created by Tsymbalyuk Konstantin from  on 15.11.2020.
 */
package com.myapp.instagramviewer.utils

import com.myapp.instagramviewer.model.InstagramMediaDataModel
import com.myapp.instagramviewer.repository.entity.InstagramMediaInfoEntity

class ModelEntityConverter {

    companion object {
        fun convertModelToEntity(model: InstagramMediaDataModel): InstagramMediaInfoEntity {
            return InstagramMediaInfoEntity(
                model.id,
                model.pageId,
                model.imagePath,
                model.imageDescription,
                model.commentCounter,
                model.likeCounter
            )
        }
    }
}