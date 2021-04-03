package me.ed333.easyBot.utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MessageChain {
    private final JSONArray json = new JSONArray();

    @Override
    public String toString() {
        return json.toString();
    }

    /**
     * 添加一个文本消息
     * @param text 文本内容
     * @return MessageChain
     */
    public MessageChain addPlain(String text) {
        this.json.element(new JSONObject().element("type", "Plain").element("text", text));
        return this;
    }

    /**
     * <p>通过图片ID来添加一个图片消息</p>
     * <p>注意的是， 好友消息的图片格式是和群图片格式不一样的：</p>
     * <p>  好友图片ID格式：xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx</p>
     * <p>  群图片ID格式：{xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx}.mirai</p>
     * @param ImageID 图片的ID
     * @return MessageChain
     */
    public MessageChain addImage(String ImageID) {
        this.json.element(new JSONObject().element("type", "Image").element("ImageId", ImageID));
        return this;
    }

    /**
     * <p>通过网络图片添加一个图片消息</p>
     * @param Url 图片的地址
     * @return MessageChain
     */
    public MessageChain addImageByUrl(String Url) {
        this.json.element(new JSONObject().element("type", "Image").element("url", Url));
        return this;
    }

    /**
     * 添加一个 At
     * @param QQ 被At的QQ
     * @return MessageChain
     */
    public MessageChain addAt(long QQ) {
        this.json.element(new JSONObject().element("type", "At").element("target", QQ));
        return this;
    }

    /**
     * 添加一个 @全体成员
     * @return MessageChain
     */
    public MessageChain addAtAll() {
        this.json.element(new JSONObject().element("type", "AtAll"));
        return this;
    }
}
