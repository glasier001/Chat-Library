package com.glasierinc.chatlibrary.chat.activity;

import static android.util.Log.e;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.JOIN_EVENT;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.REC_ALL_MESSAGE_EVENT;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.REC_MESSAGE_EVENT;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.SEND_MESSAGE_EVENT;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_CALL;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_IMAGE;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_MESSAGE;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_PDF;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.TYPE_VIDEO;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arthenica.ffmpegkit.ExecuteCallback;
import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.LogCallback;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.Session;
import com.arthenica.ffmpegkit.Statistics;
import com.arthenica.ffmpegkit.StatisticsCallback;
import com.commonlib.constants.AppUrls;
import com.commonlib.utils.StringUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.glasierinc.chatlibrary.R;
import com.glasierinc.chatlibrary.chat.adapters.MessageAdapterSocketIO;
import com.glasierinc.chatlibrary.chat.model.GetFileUploadLinkResp;
import com.glasierinc.chatlibrary.chat.model.GetMessageList;
import com.glasierinc.chatlibrary.chat.model.GetMessageListData;
import com.glasierinc.chatlibrary.chat.util.ImageFilePath;
import com.glasierinc.chatlibrary.databinding.ActivityClassRoomChatBinding;
import com.glasierinc.chatlibrary.webservice.MentalHealthWebService;
import com.glasierinc.chatlibrary.webservice.RetrofitClientInstance;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClassRoomChatActivity extends AppCompatActivity {
    private final String TAG = ClassRoomChatActivity.class.getSimpleName();
    private ActivityClassRoomChatBinding mBinding;
    String mUserId = "";
    String input_video_uri = "";
    String input_audio_uri = "";
    String roomId = "";
    String title = "";
    Uri imageUri;
    //socketIO
    Socket mSocket;
    MessageAdapterSocketIO messageAdapterSocketIO;
    private List<GetMessageListData> messages;
    String userType = "";
    String userEmail = "";
    Uri AudioUri;

    public static ClassRoomChatActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_class_room_chat);

        instance = this;


        mBinding.spinKit.setVisibility(View.GONE);
        messages = new ArrayList<>();

        userType = getIntent().getStringExtra("userType");
        userEmail = Prefs.getString("userEmail", "");


        title = getIntent().getStringExtra("title");
        mBinding.messageTitle.setText(title);
        mBinding.messageSubTitle.setText("tap here for group info");

        //fill room id
        roomId = getIntent().getStringExtra("roomId");
        mUserId = getIntent().getStringExtra("userId");


        Log.e("roomId",roomId);
        Log.e("mUserId",mUserId);
        Log.e("userType",userType);

        socketInitConnection();
        mBinding.chat.setVisibility(View.VISIBLE);

        messageAdapterSocketIO = new MessageAdapterSocketIO(this, messages, mUserId);
        mBinding.messageList.setAdapter(messageAdapterSocketIO);
        mBinding.messageList.setLayoutManager(new LinearLayoutManager(this));
        //mBinding.messageList.getLayoutManager().smoothScrollToPosition(mBinding.messageList, new RecyclerView.State(), mBinding.messageList.getAdapter().getItemCount());
        mBinding.messageList.getLayoutManager().scrollToPosition(messages.size()-1);
        onClickEvent();

    }

    private void onClickEvent() {


        mBinding.llTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassRoomChatActivity.this, ClassRoomUserListActivity.class)
                        .putExtra("roomIdChat", roomId)
                        .putExtra("userIdChat", mUserId)
                        .putExtra("userTypeChat", userType)
                        .putExtra("titleChat", title);
                startActivity(intent);
            }
        });


        mBinding.selectionChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (!mBinding.messageEdittiext.getText().toString().trim().isEmpty()) {
                        mSocket.emit(SEND_MESSAGE_EVENT, mBinding.messageEdittiext.getText().toString(), TYPE_MESSAGE);
                        messageAdapterSocketIO.notifyDataSetChanged();

                        Log.e(TAG, "messageSend");
                    } else {
                        Toast.makeText(ClassRoomChatActivity.this, "Empty message", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.e("Exp", e.getMessage());
                    Log.e(TAG, "messageSendFailed");

                }

                mBinding.messageEdittiext.setText("");
            }
        });

        mBinding.selectionImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askStoragePermission();

            }
        });


        mBinding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mSocket.disconnect();
                mSocket.off(Socket.EVENT_CONNECT, onConnect);
                mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket.off(REC_MESSAGE_EVENT, onRecMessage);
                mSocket.off(REC_ALL_MESSAGE_EVENT, onAllMessage);
                finish();
            }
        });


        mBinding.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    JitsiMeetUserInfo jitsiMeetUserInfo = new JitsiMeetUserInfo();
                    jitsiMeetUserInfo.setDisplayName(userEmail);
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                                .setServerURL(new URL("https://meet.jit.si"))
                            .setServerURL(new URL("https://jitsicall.online/"))
                            //.setServerURL(new URL("https://jitsi.glasierinc.in/"))
                            .setRoom(roomId)
                            //                            .setAudioMuted(false)
//                            .setVideoMuted(false)
                            .setAudioOnly(true)
//                            .setConfigOverride("requireDisplayName", true)
                            .setFeatureFlag("invite.enabled", false)
                            .setFeatureFlag("chat.enabled", false)
                            .setFeatureFlag("overflow-menu.enabled", false)
                            .setUserInfo(jitsiMeetUserInfo)
                            .build();


                    JitsiMeetActivity.launch(ClassRoomChatActivity.this, options);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void socketInitConnection() {
        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME};
            mSocket = IO.socket(AppUrls.CHAT_SERVER_URL, opts);
        } catch (URISyntaxException e) {
            Log.e("err", e.getMessage());
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(REC_MESSAGE_EVENT, onRecMessage);
        mSocket.connect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(REC_MESSAGE_EVENT, onRecMessage);
        mSocket.off(REC_ALL_MESSAGE_EVENT, onAllMessage);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "connecting");
                    try {

                        Log.e("roomId", roomId);
                        Log.e("mUserId", mUserId);
                        Log.e("userType", userType);
                        mSocket.emit(JOIN_EVENT, roomId, mUserId, userType);
                        Log.e(TAG, "joinRoom");
                        mSocket.on(REC_ALL_MESSAGE_EVENT, onAllMessage);

                    } catch (Exception e) {
                        Log.e("Exp", e.getMessage());
                        Log.e(TAG, "joinFailed");

                    }

                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");

                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");

                }
            });
        }
    };

    private Emitter.Listener onAllMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.e(TAG, "getAllMessages" + args[0].toString());

                    Gson gson = new Gson();
                    GetMessageList obj = gson.fromJson(args[0].toString(), GetMessageList.class);
                    messages.clear();
                    messages.addAll(obj.messageList);
                    messageAdapterSocketIO.notifyDataSetChanged();


                    //mBinding.messageList.getLayoutManager().smoothScrollToPosition(mBinding.messageList, new RecyclerView.State(), mBinding.messageList.getAdapter().getItemCount());
                    mBinding.messageList.getLayoutManager().scrollToPosition(messages.size()-1);

                }
            });
        }


    };


    private Emitter.Listener onRecMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.e(TAG, "onRecMessage" + args[0].toString());

                    Gson gson = new Gson();
                    GetMessageList obj = gson.fromJson(args[0].toString(), GetMessageList.class);
                    messages.addAll(obj.messageList);
                    messageAdapterSocketIO.notifyDataSetChanged();
                    //mBinding.messageList.getLayoutManager().smoothScrollToPosition(mBinding.messageList, new RecyclerView.State(), mBinding.messageList.getAdapter().getItemCount());
                    mBinding.messageList.getLayoutManager().scrollToPosition(messages.size()-1);

                }
            });
        }


    };


    private void askStoragePermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openBottomSheet();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {


            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage(getString(R.string.permission_denied_msg))
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(REC_MESSAGE_EVENT, onRecMessage);
        mSocket.off(REC_ALL_MESSAGE_EVENT, onAllMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                String mimeType = getContentResolver().getType(resultUri);
                Log.e("filetype", mimeType);
                final String file = resultUri.getPath();
                imageUpload(roomId, mUserId, userType, TYPE_IMAGE, file);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError().printStackTrace();
            }
        }


        if (requestCode == 1 && resultCode == RESULT_OK) {
            mBinding.spinKit.setVisibility(View.VISIBLE);
            Uri resultUri = data.getData();
            String realPath = ImageFilePath.getPath(this, resultUri);
            imageUpload(roomId, mUserId, userType, TYPE_IMAGE, realPath);


        } else if (requestCode == 0 && resultCode == RESULT_OK) {
            mBinding.spinKit.setVisibility(View.VISIBLE);

            String realPath = "";

            realPath = getFilePathFromUriGeneral(imageUri,".jpg");

            imageUpload(roomId, mUserId, userType, TYPE_IMAGE, realPath);

        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            mBinding.spinKit.setVisibility(View.VISIBLE);
            String path = getFilePathFromUriGeneral(data.getData(),".pdf");
            Log.e("path",path);
            Log.e("filesize is ", String.valueOf(checkFileSize(data.getData())));
            Long fileSizeMB = checkFileSize(data.getData());

            if (fileSizeMB>10) {
                pdfCompress(path);
            }
            else {
                imageUpload(roomId, mUserId, userType, TYPE_PDF, path);
            }

        } else if (requestCode == 4 && resultCode == RESULT_OK) {
              mBinding.spinKit.setVisibility(View.VISIBLE);
              Uri selectedImageUri = data.getData();
              input_video_uri =    getFilePathFromUriGeneral(data.getData(),".mp4");
            try {
                OutputStream out = getContentResolver().openOutputStream(selectedImageUri, "r");
                InputStream in = new FileInputStream(input_video_uri);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                // write the output file (You have now copied the file)
                out.flush();
                out.close();

            } catch (IOException e) {
                Log.d("TAG", "Error Occured" + e.getMessage());

            }

            compressVideo();


        } else if (requestCode == 5 && resultCode == RESULT_OK) {
            mBinding.spinKit.setVisibility(View.VISIBLE);
            AudioUri = data.getData();
            Log.e("Audio Selected", AudioUri.toString());
            input_audio_uri =    getFilePathFromUriGeneral(AudioUri,".mp3");
            imageUpload(roomId, mUserId, userType, TYPE_CALL, input_audio_uri);
        }


    }


    public String getFilePathFromUriGeneral(Uri uri, String exe) {
        String filePath = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File file = new File(getCacheDir(),  System.currentTimeMillis() + exe); // or "temp.doc"
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            filePath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private void pdfCompress(String realPath) {
        PdfReader reader = null;
        File folder = getCacheDir();
        File file = new File(folder, System.currentTimeMillis() + ".pdf");
        try {
            reader = new PdfReader(new FileInputStream(realPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert reader != null;
        Document document = new Document(reader.getPageSizeWithRotation(1));
        PdfCopy writer = null;

        try {
            writer = new PdfCopy(document, new FileOutputStream(file.getAbsolutePath()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
        assert writer != null;

        Log.e("pagesize", reader.getNumberOfPages() + "");
        int total = 0;
        if (reader.getNumberOfPages() > 1) {
            total = reader.getNumberOfPages();

        }

        for (int i = 1; i < total; i++) {
            PdfImportedPage page = writer.getImportedPage(reader, ++i);
            try {
                writer.addPage(page);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BadPdfFormatException e) {
                e.printStackTrace();
            }
        }

        try {
            writer.setFullCompression();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();
        writer.close();

        String formated_size = Formatter.formatShortFileSize(ClassRoomChatActivity.this, file.length());

        Log.e("size is ", formated_size + file.getAbsolutePath());
        // imageUpload(file.getAbsolutePath());
        imageUpload(roomId, mUserId, userType, TYPE_PDF, file.getAbsolutePath());

    }

    private void compressVideo() {

        File folder = getCacheDir();
        File file = new File(folder, System.currentTimeMillis() + ".mp4");

        String[] exe = {"-y", "-i", input_video_uri, "-s", "480x720", "-r", "25", "-vcodec", "mpeg4", "-b:v", "150k", "-b:a", "48000", "-ac", "2", "-ar", "22050", file.getAbsolutePath()};

        FFmpegKit.executeAsync(exe, new ExecuteCallback() {
            @Override
            public void apply(Session session) {
                ReturnCode returnCode = session.getReturnCode();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (returnCode.isSuccess()) {

                            input_video_uri = file.getAbsolutePath();
                            String temp = URLDecoder.decode(input_video_uri);
                            String videopath = temp;
                            Log.e("path", videopath);

                            File file = new File(videopath);
                            String formated_size = Formatter.formatShortFileSize(ClassRoomChatActivity.this, file.length());

                            Log.e("size is ", formated_size);


                            mBinding.spinKit.setVisibility(View.GONE);

                            Toast.makeText(ClassRoomChatActivity.this, "Filter Applied", Toast.LENGTH_SHORT).show();

                            // imageUpload(videopath);
                            imageUpload(roomId, mUserId, userType, TYPE_VIDEO, videopath);

                        } else {
                            mBinding.spinKit.setVisibility(View.GONE);
                            Log.d("TAG", session.getAllLogsAsString());
                            Toast.makeText(ClassRoomChatActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }, new LogCallback() {
            @Override
            public void apply(com.arthenica.ffmpegkit.Log log) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        progressDialog.setMessage("Applying Filter..\n"+log.getMessage());

                        mBinding.spinKit.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, new StatisticsCallback() {
            @Override
            public void apply(Statistics statistics) {

//                android.util.Log.d("STATS", statistics.toString());

            }
        });


    }

    private Long checkFileSize(Uri uri) {
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = getContentResolver().openAssetFileDescriptor(uri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Long fileSize = fileDescriptor.getLength();

        Long fileSizeInKB = fileSize / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;

        return fileSizeInMB;
    }


    public String getFileExtension(String filePath) {
        String extension = "";
        try {
            extension = filePath.substring(filePath.lastIndexOf("."));
        } catch (Exception exception) {
            e("Err", exception.toString() + "");
        }
        return extension;
    }

    void imageUpload(String roomId, String mUserId, String userType, String fileType, String attach_file) {
        MultipartBody.Part body = null;


        if (StringUtils.isNotNullNotEmpty(attach_file)) {

            // creates RequestBody instance from file
            RequestBody requestFile = RequestBody.create(new File(attach_file), MediaType.parse(getFileExtension(attach_file)));
            // MultipartBody.Part is used to send also the actual filename

            body = MultipartBody.Part.createFormData("attach_file", new File(attach_file).getName(), requestFile);
            Log.e("filename",attach_file);
        }


        RequestBody roomIdReq = RequestBody.create(MediaType.parse("text/plain"),
                roomId);
        RequestBody mUserIdReq = RequestBody.create(MediaType.parse("text/plain"),
                mUserId);

        RequestBody userTypeReq = RequestBody.create(MediaType.parse("text/plain"),
                userType);
        RequestBody fileTypeReq = RequestBody.create(MediaType.parse("text/plain"),
                fileType);


        Log.e("roomId",roomId);
        Log.e("mUserId",mUserId);
        Log.e("userType",userType);
        Log.e("fileType",fileType);



        RetrofitClientInstance.getRetrofitClassRoom().create(MentalHealthWebService.class).getImageUrl(roomIdReq, mUserIdReq, userTypeReq, fileTypeReq, body)
                .enqueue(new Callback<GetFileUploadLinkResp>() {

                    @Override
                    public void onResponse(Call<GetFileUploadLinkResp> call, Response<GetFileUploadLinkResp> response) {

                        if (response.code() == 200) {
                            assert response.body() != null;
                            if (response.body().status) {
                                e("url", response.body().message_list.attach_file);


                                if (response.body().message_list.attach_file.contains(".mp4")) {
//                                    recFileType = "video";

                                    //  mSocket.emit(SEND_MESSAGE_EVENT, roomId, mUserId, response.body().message_list.attach_file, TYPE_VIDEO);
                                    mSocket.emit(SEND_MESSAGE_EVENT, response.body().message_list.attach_file, TYPE_VIDEO);

                                } else if (response.body().message_list.attach_file.contains(".jpg") ||
                                        response.body().message_list.attach_file.contains(".jpeg") ||
                                        response.body().message_list.attach_file.contains(".png")) {
                                    //   mSocket.emit(SEND_MESSAGE_EVENT, roomId, mUserId, response.body().message_list.attach_file, TYPE_IMAGE);
                                    mSocket.emit(SEND_MESSAGE_EVENT, response.body().message_list.attach_file, TYPE_IMAGE);

                                } else if (response.body().message_list.attach_file.contains(".pdf")) {
                                    //  mSocket.emit(SEND_MESSAGE_EVENT, roomId, mUserId, response.body().data.chat_file_url, TYPE_PDF);
                                    mSocket.emit(SEND_MESSAGE_EVENT, response.body().message_list.attach_file, TYPE_PDF);

                                } else if (response.body().message_list.attach_file.contains(".mp3")) {
                                    //  mSocket.emit(SEND_MESSAGE_EVENT, roomId, mUserId, response.body().data.chat_file_url, TYPE_PDF);
                                    mSocket.emit(SEND_MESSAGE_EVENT, response.body().message_list.attach_file, TYPE_CALL);

                                }

                                messageAdapterSocketIO.notifyDataSetChanged();
                               // mBinding.messageList.getLayoutManager().smoothScrollToPosition(mBinding.messageList, new RecyclerView.State(), mBinding.messageList.getAdapter().getItemCount());
                                mBinding.messageList.getLayoutManager().scrollToPosition(messages.size()-1);


                                /*    RtmMessage message = mRtmClient.createMessage();
                                message.setText(response.body().data.chat_file_url);
                                MessageBean messageBean = new MessageBean(mUserId, message, true, recFileType);
                                mMessageBeanList.add(messageBean);
                                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                                mBinding.messageList.scrollToPosition(mMessageBeanList.size() - 1);

                                sendPeerMessage(message);*/
                            }
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(ClassRoomChatActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(ClassRoomChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        }
                        mBinding.spinKit.setVisibility(View.GONE);


                    }

                    @Override
                    public void onFailure(Call<GetFileUploadLinkResp> call, Throwable t) {
                        call.cancel();
                        mBinding.spinKit.setVisibility(View.GONE);

                    }
                });

    }

    private void openBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.choose_camera_gallery_bottomsheet_option);

        TextView ivCamera = bottomSheetDialog.findViewById(R.id.ivCamera);
        TextView ivGallery = bottomSheetDialog.findViewById(R.id.ivGallery);
        TextView tvPdf = bottomSheetDialog.findViewById(R.id.tvPdf);
        TextView tvVideo = bottomSheetDialog.findViewById(R.id.tvVideo);
        TextView tvAudio = bottomSheetDialog.findViewById(R.id.tvAudio);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                values.clear();
                startActivityForResult(takePicture, 0);

                bottomSheetDialog.dismiss();
            }
        });


        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mimetypes = {"image/*", "application/pdf", "video/*"};
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");
                startActivityForResult(pickIntent, 1);


                bottomSheetDialog.dismiss();
            }
        });

        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("video/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.addFlags(
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                                | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
                startActivityForResult(intent, 4);


                bottomSheetDialog.dismiss();
            }
        });

        tvAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent audio = new Intent();
                audio.setType("audio/*");
                audio.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(audio, "Select Audio"), 5);

                bottomSheetDialog.dismiss();

            }
        });


        tvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, 3);

                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }


}