package com.aoezdemir.todoapp.activity.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.activity.EditActivity;
import com.aoezdemir.todoapp.model.Todo;
import com.aoezdemir.todoapp.utils.ContactUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private static final String TAG = ContactAdapter.class.getSimpleName();

    private Todo todo;
    private Boolean editMode;
    private ContentResolver resolver;
    private AppCompatActivity activity;

    public ContactAdapter(Todo todo, Boolean editMode, ContentResolver resolver, AppCompatActivity activity) {
        this.todo = todo;
        this.editMode = editMode;
        this.resolver = resolver;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_contact, parent, false), this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        List<String> contacts = todo.getContacts();
        if (contacts != null &&
                !contacts.isEmpty() &&
                position < contacts.size()) {
            holder.loadContact(contacts.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        List<String> contacts = todo.getContacts();
        if (contacts != null && !contacts.isEmpty()) {
            return contacts.size();
        }
        return 0;
    }

    public void setContacts(List<String> contacts) {
        todo.setContacts(contacts);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ContactAdapter adapter;
        private ImageView ivContactImage;
        private TextView tvContactName;
        private TextView tvContactTelephone;
        private TextView tvContactEmail;
        private ImageButton ibContactDelete;
        private ImageButton ibContactSendEmail;
        private ImageButton ibContactSendSms;

        ContactViewHolder(View v, ContactAdapter a) {
            super(v);
            view = v;
            adapter = a;
            ivContactImage = view.findViewById(R.id.ivContactImage);
            tvContactName = view.findViewById(R.id.tvContactName);
            tvContactTelephone = view.findViewById(R.id.tvContactTelephone);
            tvContactEmail = view.findViewById(R.id.tvContactEmail);
            ibContactDelete = view.findViewById(R.id.ibContactDelete);
            ibContactSendEmail = view.findViewById(R.id.ibContactSendEmail);
            ibContactSendSms = view.findViewById(R.id.ibSendSms);
        }

        private void initContactImage(String contactId) {
            try {
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(resolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId)));
                if (inputStream != null) {
                    ivContactImage.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.i(TAG, "Failed to build a Bitmap from contact image.");
            }
        }

        private void initContactDelete(int position) {
            if (editMode != null && editMode) {
                ibContactDelete.setVisibility(View.VISIBLE);
                ibContactDelete.setOnClickListener((View v) -> {
                    todo.getContacts().remove(position);
                    adapter.notifyDataSetChanged();
                    ((EditActivity) activity).enableSaveButton();
                });
            } else {
                ibContactDelete.setVisibility(View.INVISIBLE);
            }
        }

        private void initContactSendEmail(String email) {
            if (editMode == null || email == null) {
                ibContactSendEmail.setVisibility(View.INVISIBLE);
            } else {
                if (editMode) {
                    ibContactSendEmail.setVisibility(View.INVISIBLE);
                } else {
                    ibContactSendEmail.setVisibility(View.VISIBLE);
                    ibContactSendEmail.setOnClickListener((View v) -> {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        intent.putExtra(Intent.EXTRA_SUBJECT, todo.getName());
                        intent.putExtra(Intent.EXTRA_TEXT, todo.getDescription());
                        activity.startActivity(intent);
                    });
                }
            }
        }

        private void initContactSendSms(String phone) {
            if (editMode == null || phone == null) {
                ibContactSendSms.setVisibility(View.INVISIBLE);
            } else {
                if (editMode) {
                    ibContactSendSms.setVisibility(View.INVISIBLE);
                } else {
                    ibContactSendSms.setVisibility(View.VISIBLE);
                    ibContactSendSms.setOnClickListener((View v) -> {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                        intent.putExtra("sms_body", todo.getDescription());
                        activity.startActivity(intent);
                    });
                }
            }
        }

        void loadContact(String contact, int position) {
            if (contact != null && !contact.isEmpty()) {
                String contentId = contact.split(";")[0];
                String contentName = contact.split(";")[1];
                String email = ContactUtils.getContactEmailById(resolver, contentId);
                String phone = ContactUtils.getContactPhoneById(resolver, contentId);

                initContactImage(contentId);
                tvContactName.setText(contentName);
                tvContactTelephone.setText(phone);
                tvContactEmail.setText(email);
                initContactDelete(position);
                initContactSendEmail(email);
                initContactSendSms(phone);
            }
        }
    }
}